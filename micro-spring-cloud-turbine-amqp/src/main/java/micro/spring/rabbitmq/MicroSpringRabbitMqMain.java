package micro.spring.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;

/**
 * @Author: shengbin
 * @since: 2019/4/28 下午6:44
 */
@EnableTurbineStream
@EnableDiscoveryClient
@SpringBootApplication
public class MicroSpringRabbitMqMain {
    public static void main(String[] args) {
        SpringApplication.run(MicroSpringRabbitMqMain.class, args);
    }
}
