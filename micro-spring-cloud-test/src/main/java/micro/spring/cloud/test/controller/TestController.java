package micro.spring.cloud.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shengbin
 * @since: 2019/4/17 下午11:26
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @GetMapping(value = "/getHello")
    public String helloTest() {
        return "hello SpringBoot!!!";
    }
}
