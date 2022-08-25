package micro.spring.cloud.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author: shengbin
 * @since: 2019/4/18 下午11:44
 */
@EnableEurekaServer
@SpringBootApplication
public class SpringCloudEurekaServerMain {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudEurekaServerMain.class, args);
    }
}

