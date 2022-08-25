package sharding.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sharding.test.entity.User;
import sharding.test.repository.UserRepository;

/**
 * @author: wangshengbin
 * @date: 2020/6/5 下午12:00
 */
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public Object list() {
        return userRepository.findAll();
    }

    @GetMapping("/add")
    public Object add() {
        for (long i = 0; i < 100; i++) {
            User user = new User();
            user.setId(i);
            user.setCity("深圳");
            user.setName("李四");
            userRepository.save(user);
        }
        return "success";
    }

    @GetMapping("/users/{id}")
    public Object get(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @GetMapping("/users/query")
    public Object get(String name) {
        return userRepository.findByName(name);
    }

}