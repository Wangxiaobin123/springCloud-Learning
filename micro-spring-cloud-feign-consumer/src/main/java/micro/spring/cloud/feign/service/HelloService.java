package micro.spring.cloud.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * instance name
 *
 * @Author: shengbin
 * @since: 2019/4/23 下午10:40
 */
@FeignClient("service-api")
public interface HelloService {

    /**
     * no params
     *
     * @return
     */
    @GetMapping(value = "/hello")
    String hello();
}

