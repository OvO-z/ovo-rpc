package github.ovo.service;

import github.ovo.Hello;
import github.ovo.HelloService;
import github.ovo.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * HelloService 实现类
 * @author QAQ
 * @date 2021/9/18
 */

@RpcService(version = "1.0.0", group = "group1")
@Slf4j
public class HelloServiceImpl implements HelloService {

    static {
        log.info("init helloService");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
