package cbfan.server;

import cbfan.cfg.SpringCfg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/4/27.
 */
@Component
public class Main {


    public static void main(String[] args) {
        AbstractApplicationContext ctx =
                new AnnotationConfigApplicationContext(SpringCfg.class);
        ctx.registerShutdownHook();
        TCPServer tcpServer = new TCPServer();
        tcpServer.start();

    }
}
