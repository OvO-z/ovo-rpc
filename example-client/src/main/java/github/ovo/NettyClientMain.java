package github.ovo;

import github.ovo.annotation.RpcScan;
import lombok.SneakyThrows;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author QAQ
 * @date 2021/9/18
 */

@RpcScan(basePackage = "github.ovo")
public class NettyClientMain {
    @SneakyThrows
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");

        helloController.test();
    }
}
