package cbfan.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/4/26.
 */
@Component
public class ChatChannelInitalizer extends ChannelInitializer<SocketChannel> {

    @Autowired
    @Qualifier("lengthFieldBasedFrameDecoder")
    private LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder;

    @Autowired
    private NettyMsgAdapter nettyMsgAdapter;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("msgDecode", lengthFieldBasedFrameDecoder);
        pipeline.addLast("msgHandle", nettyMsgAdapter);
    }
}
