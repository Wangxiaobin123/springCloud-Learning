package micro.spring.cloud.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @Author: shengbin
 * @since: 2019/4/27 下午10:32
 */
@EnableDiscoveryClient
@EnableHystrixDashboard
@SpringCloudApplication
public class MicroHystrixDashboardMain {
    public static void main(String[] args) {
        SpringApplication.run(MicroHystrixDashboardMain.class, args);
    }
}
