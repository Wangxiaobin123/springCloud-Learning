package micro.spring.cloud.feign.controller;

import micro.spring.boot.query.dto.User;
import micro.spring.cloud.feign.service.HelloService;
import micro.spring.cloud.feign.service.RefactorHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: shengbin
 * @since: 2019/4/23 下午10:45
 */
//@RestController
public class HelloConsumerController {

    @Autowired
    private HelloService helloService;

    @Autowired
    private RefactorHelloService refactorHelloService;

    @GetMapping(value = "/feign-consumer")
    public String helloConsumer() {
        return helloService.hello();
    }


    @GetMapping(value = "/feign-consumer1")
    public String helloConsumer1(@RequestParam("name") String name) {
        return refactorHelloService.hello(name);
    }


    @GetMapping(value = "/feign-consumer2")
    public User helloConsumer2(@RequestHeader("name") String name, @RequestHeader("age") Integer age) {
        return refactorHelloService.hello(name, age);
    }

    @PostMapping(value = "/feign-consumer3")
    public String helloConsumer3(@RequestBody User user) {
        return refactorHelloService.hello(user);
    }

}