package test.apollo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import test.apollo.config.ApolloAutoConfig;

import javax.annotation.Resource;

/**
 * @author: wangshengbin
 * @date: 2020/3/28 下午10:42
 */
@RestController(value = "/demo")
@Slf4j
public class DemoApolloController {

    @Resource
    private ApolloAutoConfig apolloAutoConfig;

    @GetMapping(value = "/getConfig")
    public String getConfig() {
        return apolloAutoConfig.getStr1() + "========" + apolloAutoConfig.getServerPort() + "======" + apolloAutoConfig.getRedisConfigAddr();
    }
}
