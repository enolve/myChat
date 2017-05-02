package cbfan.cfg;

import cbfan.handler.ChatChannelInitalizer;
import cbfan.handler.HttpRequestHandler;
import cbfan.handler.WebSocketAdapterHandler;
import cbfan.handler.WebSocketInitalizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/26.
 */
@Configuration
@ComponentScan("cbfan")
@PropertySource("classpath:netty_server.properties")
public class SpringCfg {
    @Value("8888")
    private int port;

    @Value("2")
    private int bossThreadCount;

    @Value("2")
    private int workThreadCount;

    @Value("true")
    private boolean keepAlive;

    @Value("100")
    private int backlog;

    @Value("60")
    private int timeout;

    @Autowired
    private ChatChannelInitalizer chatChannelInitalizer;

    @Autowired
    private WebSocketInitalizer webSocketInitalizer;

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public EventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossThreadCount);
    }
    @Bean(name = "workGroup", destroyMethod = "shutdownGracefully")
    public EventLoopGroup workGroup() {
        return new NioEventLoopGroup(workThreadCount);
    }

    @Bean
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(port);
    }

    @Bean(name = "tcpOpt")
    public Map<ChannelOption<?>, Object> tcpOpt() {
        Map<ChannelOption<?>, Object> opt = new HashMap<>();
        opt.put(ChannelOption.SO_KEEPALIVE, keepAlive);
        opt.put(ChannelOption.SO_BACKLOG, backlog);
        return opt;
    }

    @Bean
    public ServerBootstrap serverBootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup(), workGroup())
                .channel(NioServerSocketChannel.class).childHandler(webSocketInitalizer);
        Map<ChannelOption<?>, Object> tcpChannelOptions = tcpOpt();
        Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
        for (ChannelOption option : keySet) {
            serverBootstrap.option(option, tcpChannelOptions.get(option));
        }
        return serverBootstrap;
    }

    @Bean(name = "lengthFieldBasedFrameDecoder")
    public LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder() {
        return new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 200 * 1024, 0, 4, -4, 0, true);
    }

    //----------WebSocket----------------------
    @Bean
    public HttpServerCodec httpServerCodec() {
        return new HttpServerCodec();
    }

    @Bean
    public HttpObjectAggregator httpObjectAggregator() {
        return new HttpObjectAggregator(64 * 1024);
    }

    @Bean
    public ChunkedWriteHandler chunkedWriteHandler() {
        return new ChunkedWriteHandler();
    }

    @Bean
    public WebSocketServerProtocolHandler webSocketServerProtocolHandler() {
        return new WebSocketServerProtocolHandler("/ws");
    }

    @Bean
    public HttpRequestHandler httpRequestHandler() {
        return new HttpRequestHandler();
    }

    @Bean
    public WebSocketAdapterHandler webSocketAdapterHandler() {
        return new WebSocketAdapterHandler();
    }

}
