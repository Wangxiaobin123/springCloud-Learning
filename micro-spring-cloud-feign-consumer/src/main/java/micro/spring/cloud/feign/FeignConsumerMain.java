package micro.spring.cloud.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: shengbin
 * @since: 2019/4/23 下午10:24
 */
//@EnableSwagger2Doc
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class FeignConsumerMain {
    public static void main(String[] args) {
        SpringApplication.run(FeignConsumerMain.class, args);
    }
}