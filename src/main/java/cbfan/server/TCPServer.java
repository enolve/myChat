package cbfan.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2017/4/27.
 */
@Component
@Qualifier("tcpServer")
public class TCPServer {

    @Autowired
    private ServerBootstrap serverBootstrap;

    @Autowired
    private InetSocketAddress tcpPort;

    public void start() {
        ChannelFuture channelFuture = serverBootstrap.bind(tcpPort).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("linsten " + tcpPort.getPort() + "succ");
                } else {
                    System.out.println("linsten " + tcpPort.getPort() + "fail");
                }
            }
        });
        try {
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
