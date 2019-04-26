package micro.spring.cloud.feign.controller;

import micro.spring.boot.query.dto.User;
import micro.spring.cloud.feign.service.HelloService;
import micro.spring.cloud.feign.service.RefactorHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shengbin
 * @since: 2019/4/23 下午10:45
 */
@RestController
public class HelloConsumerController {

    @Autowired
    private HelloService helloService;

    @Autowired
    private RefactorHelloService refactorHelloService;

    @GetMapping(value = "/feign-consumer")
    public String helloConsumer() {
        return helloService.hello();
    }


    @GetMapping(value = "/feign-consumer3")
    public String helloConsumer3() {
        return refactorHelloService.hello("MIMI") + "\n" +
                refactorHelloService.hello("MIMI", 20) + "\n" +
                refactorHelloService.hello(new User("MIMI", 20)) + "\n";
    }

}