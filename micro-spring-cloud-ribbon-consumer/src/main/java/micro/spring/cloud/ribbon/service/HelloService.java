package micro.spring.cloud.ribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: shengbin
 * @since: 2019/4/27 下午5:51
 */
@Service
@Slf4j
public class HelloService {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 设置回调方法
     *
     * @return
     */
    @HystrixCommand(fallbackMethod = "helloFallback")
    public String HelloService() {
        return restTemplate.getForEntity("http://SERVICE-API/hello", String.class).getBody();
    }

    public String helloFallback() {
        return "maybe one instance is down,please check!!!";
    }
}
