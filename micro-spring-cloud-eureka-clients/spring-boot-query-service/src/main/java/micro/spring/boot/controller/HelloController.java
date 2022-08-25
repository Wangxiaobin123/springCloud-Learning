package micro.spring.boot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: shengbin
 * @since: 2019/4/21 下午12:04
 */
@RestController
@Slf4j
public class HelloController {

    @Autowired
    private Registration registration;
    @Autowired
    private DiscoveryClient client;

    @GetMapping(value = "/hello")
    public String index() {
        List<ServiceInstance> serviceInstanceList = client.getInstances(registration.getServiceId());
        log.info("serviceInstanceList size = {}", serviceInstanceList.size());
        return "hello world";
    }
}