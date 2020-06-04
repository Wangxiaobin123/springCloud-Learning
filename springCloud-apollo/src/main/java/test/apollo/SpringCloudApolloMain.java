package test.apollo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: wangshengbin
 * @date: 2020/3/28 下午10:37
 */
@SpringBootApplication
@EnableSwagger2
public class SpringCloudApolloMain {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudApolloMain.class,args);
    }
}
