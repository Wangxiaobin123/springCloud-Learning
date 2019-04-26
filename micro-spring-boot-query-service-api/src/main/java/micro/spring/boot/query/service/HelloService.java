package micro.spring.boot.query.service;

import micro.spring.boot.query.dto.User;
import org.springframework.web.bind.annotation.*;

/**
 * feign and rest all extends this service
 *
 * @Author: shengbin
 * @since: 2019/4/26 上午12:47
 */
@RequestMapping("/refactor")
public interface HelloService {

    /**
     * trans for name of params
     *
     * @param name
     * @return
     */
    @GetMapping(value = "/hello4")
    String hello(@RequestParam("name") String name);

    /**
     * trans for name and age of params
     *
     * @param name
     * @param age
     * @return
     */
    @GetMapping(value = "/hello5")
    User hello(@RequestHeader("name") String name, @RequestHeader("age") Integer age);

    /**
     * trans for object param
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/hello6")
    String hello(@RequestBody User user);
}
