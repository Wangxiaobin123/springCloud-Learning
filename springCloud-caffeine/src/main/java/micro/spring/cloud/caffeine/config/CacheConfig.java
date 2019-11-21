package micro.spring.cloud.caffeine.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author: shengbin
 * @since: 2019/11/21 下午6:07
 */
@Configuration
public class CacheConfig {

    @Bean
    public Cache cacheLoader() {
        return Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .maximumSize(5)
                .removalListener(((key, value, cause) -> {
                    // 清理通知 key,value ==> 键值对   cause ==> 清理原因
                    System.out.println("key = " + key + ",value = " + value + ",cause = " + cause);
                }))
                .build();
    }

}
