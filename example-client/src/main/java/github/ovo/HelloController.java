package github.ovo;

import github.ovo.annotation.RpcReference;
import org.springframework.stereotype.Component;

/**
 * @author QAQ
 * @date 2021/9/22
 */

@Component
public class HelloController {

    @RpcReference(version = "1.0.0", group = "group1")
    private HelloService helloService;

    public void test() throws InterruptedException {
        String hello = helloService.hello(new Hello("111", "222"));
        Thread.sleep(10000);
        for (int i = 0; i < 10; i++) {
            System.out.println(helloService.hello(new Hello("111", "222")));
        }
    }
}
