package micro.spring.boot.controller;

import micro.spring.boot.query.dto.User;
import micro.spring.boot.query.service.HelloService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shengbin
 * @since: 2019/4/26 下午1:36
 */
@RestController
public class RefactorHelloController implements HelloService {
    /**
     * trans for name of params
     *
     * @param name
     * @return
     */
    @Override
    public String hello(@RequestParam("name") String name) {
        return "hell one param";
    }

    /**
     * trans for name and age of params
     *
     * @param name
     * @param age
     * @return
     */
    @Override
    public User hello(@RequestHeader("name") String name, @RequestHeader("age") Integer age) {
        return new User(name, age);
    }

    /**
     * trans for object param
     *
     * @param user
     * @return
     */
    @Override
    public String hello(@RequestBody User user) {
        return "name = " + user.getName() + ",age = " + user.getAge();
    }
}
