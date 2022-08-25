package micro.spring.cloud.turbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * @Author: shengbin
 * @since: 2019/4/27 下午11:23
 */
@EnableTurbine
@EnableDiscoveryClient
@SpringBootApplication
public class MicroTurbineMain {
    public static void main(String[] args) {
        SpringApplication.run(MicroTurbineMain.class, args);
    }
}
