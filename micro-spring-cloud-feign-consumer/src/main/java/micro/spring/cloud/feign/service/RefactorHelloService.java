package micro.spring.cloud.feign.service;

import micro.spring.boot.query.service.HelloService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * FeignClient: instance name
 *
 * @Author: shengbin
 * @since: 2019/4/26 下午1:23
 */
@FeignClient("service-api")
public interface RefactorHelloService extends HelloService {
}
