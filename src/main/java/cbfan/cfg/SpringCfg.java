package cbfan.cfg;

import cbfan.handler.ChatChannelInitalizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Value("${tcp.port}")
    private int port;

    @Value("${boss.thread.count}")
    private int bossThreadCount;

    @Value("${work.thread.count}")
    private int workThreadCount;

    @Value("${tcp.keepalive}")
    private boolean keepAlive;

    @Value("${tcp.backlog}")
    private int backlog;

    @Value("${timeout}")
    private int timeout;

    @Autowired
    @Qualifier("springChatChannelInitalizer")
    private ChatChannelInitalizer chatChannelInitalizer;

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

    @Bean(name = "tcpOpt", destroyMethod = "shutdownGracefully")
    public Map<ChannelOption<?>, Object> tcpOpt() {
        Map<ChannelOption<?>, Object> opt = new HashMap<>();
        opt.put(ChannelOption.SO_KEEPALIVE, keepAlive);
        opt.put(ChannelOption.SO_BACKLOG, backlog);
        return opt;
    }

    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup(), workGroup())
                .channel(NioServerSocketChannel.class).childHandler(chatChannelInitalizer);
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

}
