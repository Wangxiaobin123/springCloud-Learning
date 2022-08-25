package ali.spring.cloud.micro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: shengbin
 * @since: 2019/5/12 上午9:10
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AliClientCloudMain {

    public static void main(String[] args) {
        SpringApplication.run(AliClientCloudMain.class, args);
    }

    @Slf4j
    @RestController
    static class TestController {

        @Autowired
        LoadBalancerClient loadBalancerClient;

        @GetMapping("/test")
        public String test() {
            // 通过spring cloud common中的负载均衡接口选取服务提供节点实现接口调用
            ServiceInstance serviceInstance = loadBalancerClient.choose("alibaba-nacos-discovery-server");
            String url = serviceInstance.getUri() + "/hello?name=" + "didi";
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            return "Invoke : " + url + ", return : " + result;
        }
    }

    @Slf4j
    @RestController
    @RefreshScope
    static class TestController2 {

        @Value("${didispace.title:}")
        private String title;

        @GetMapping("/test2")
        public String hello() {
            return title;
        }

    }

    /**
     * 多文件加载的例子使用的验证接口
     * <p>
     * blog: http://blog.didispace.com/spring-cloud-alibaba-nacos-config-3/
     */
    @Slf4j
    @RestController
    @RefreshScope
    static class Test2Controller {

        @Value("${didispace.title:}")
        private String title;
        @Value("${aaa:}")
        private String aaa;
        @Value("${bbb:}")
        private String bbb;

        @GetMapping("/test3")
        public String test2() {
            return title + ", " + aaa + ", " + bbb;
        }

    }
}
