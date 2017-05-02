package cbfan.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by acer on 2017/4/28.
 */
@Component
public class WebSocketInitalizer extends ChannelInitializer<Channel>{

    @Autowired
    private HttpServerCodec httpServerCodec;

    @Autowired
    private HttpObjectAggregator httpObjectAggregator;

    @Autowired
    private ChunkedWriteHandler chunkedWriteHandler;

    @Autowired
    private HttpRequestHandler httpRequestHandler;

    @Autowired
    private WebSocketServerProtocolHandler webSocketServerProtocolHandler;

    @Autowired
    private WebSocketAdapterHandler webSocketAdapterHandler;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(httpServerCodec)
                .addLast(httpObjectAggregator)
                .addLast(chunkedWriteHandler)
                .addLast(httpRequestHandler)
                .addLast(webSocketServerProtocolHandler)
                .addLast(webSocketAdapterHandler);
    }

}
