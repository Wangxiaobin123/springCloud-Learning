package sharding.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: wangshengbin
 * @date: 2020/6/4 下午6:20
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})

public class ShardingDbTableApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingDbTableApplication.class, args);

    }
}
