package micro.spring.boot;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: shengbin
 * @since: 2019/4/21 上午11:07
 */
@EnableSwagger2Doc
@EnableDiscoveryClient
@SpringBootApplication
public class SpringBootServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootServiceMain.class, args);
    }
}
