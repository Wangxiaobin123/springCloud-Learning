package micro.spring.cloud.caffeine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: shengbin
 * @since: 2019/11/21 下午2:38
 */
@SpringBootApplication
@EnableSwagger2
public class SpringCloudCaffeineMain {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudCaffeineMain.class,args);
    }

}
