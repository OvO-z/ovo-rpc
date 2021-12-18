package github.ovo;

import github.ovo.provider.ServiceProvider;
import github.ovo.provider.ServiceProviderImpl;
import github.ovo.remoting.transport.netty.server.NettyRpcServer;
import github.ovo.service.HelloServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author QAQ
 * @date 2021/9/21
 */

public class NettyServerMainWithOutAnnotation {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
        NettyRpcServer nettyServer = applicationContext.getBean(NettyRpcServer.class);
        nettyServer.start();
        ServiceProvider serviceProvider = new ServiceProviderImpl();
        serviceProvider.publishService(helloService);
    }
}
