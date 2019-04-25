package micro.spring.cloud.feign.controller;

import micro.spring.cloud.feign.service.HelloService;
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

    @GetMapping(value = "/feign-consumer")
    public String helloConsumer() {
        return helloService.hello();
    }


}