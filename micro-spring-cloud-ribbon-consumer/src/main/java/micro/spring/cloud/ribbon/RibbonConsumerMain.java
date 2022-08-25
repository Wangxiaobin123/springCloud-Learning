package micro.spring.cloud.ribbon;


import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1)EnableDiscoveryClient: find Service
 * 2)EnableCircuitBreaker: 断路器功能开启
 *
 * @Author: shengbin
 * @since: 2019/4/21 下午5:12
 */
@EnableSwagger2Doc
//@EnableCircuitBreaker
//@EnableDiscoveryClient
@SpringBootApplication
public class RibbonConsumerMain {

    // 客户端负载均衡
//    @Bean
//    @LoadBalanced
//    RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    public static void main(String[] args) {
        SpringApplication.run(RibbonConsumerMain.class, args);
    }

}

