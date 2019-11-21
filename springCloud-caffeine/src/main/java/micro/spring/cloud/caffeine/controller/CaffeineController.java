package micro.spring.cloud.caffeine.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import micro.spring.cloud.caffeine.service.CacheService;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: shengbin
 * @since: 2019/11/21 下午3:26
 */
@RestController("/caffeine")
@Slf4j
public class CaffeineController {

    @Autowired
    private Cache cache;
    @Autowired
    private CacheService cacheService;

    @GetMapping(value = "/getValueLoad")
    public void getValueFromCaffeineLoad(@RequestParam(value = "key") String key) {
        // 此时的类型是 LoadingCache 不是 Cache
        LoadingCache<String, Integer> cache = Caffeine.newBuilder().build(value -> {
            System.out.println("自动填充:" + value);
            return 18;
        });

        Integer age1 = cache.getIfPresent(key);
        System.out.println(age1);
    }


    @GetMapping(value = "/getValueManual")
    public void getValueFromCaffeineManual(@RequestParam(value = "key") String keyName) {
        Cache<String, Integer> cache = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                //最大容量1024个，超过会自动清理空间
                .maximumSize(5)
                .removalListener(((key, value, cause) -> {
                    // 清理通知 key,value ==> 键值对   cause ==> 清理原因
                    System.out.println("key = " + key + ",value = " + value + ",cause = " + cause);
                }))
                .build();
        log.info("cache size:{}", cache.asMap().size());

        Integer age1 = cache.getIfPresent(keyName);
        System.out.println(age1);

        for (int i = 0; i < 10; i++) {
            cache.put("" + i, i);
        }

        for (int i = 0; i < 4; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("cache :{}", cache.asMap());
        }

//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // 当key不存在时，会立即创建出对象来返回，age2不会为空
        Integer age2 = cache.get(keyName, k -> {
            System.out.println("k:" + k);
            return 18;
        });
        System.out.println(age2);
    }

    @GetMapping(value = "/getValueManualCount")
    public void getValueFromCaffeineManualCount(@RequestParam(value = "key") String keyName) {
        Cache<String, Integer> cache = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.SECONDS)
                //最大容量1024个，超过会自动清理空间
                .maximumSize(1024)
//                .removalListener(((key, value, cause) -> {
//                    // 清理通知 key,value ==> 键值对   cause ==> 清理原因
//                    System.out.println("key = " + key + ",value = " + value + ",cause = " + cause);
//                }))
                .build();
        Integer age1 = cache.getIfPresent(keyName);
        System.out.println(age1);

        for (int i = 0; i < 10; i++) {
            @Nullable Integer count = cache.getIfPresent(i + "");
            if (null == count) {
                cache.put("" + i, 0);
            }
        }
        log.info("cache1 :{}", cache.asMap());

        for (int i = 4; i < 11; i++) {
            @Nullable Integer count = cache.getIfPresent(i + "");
            if (null == count) {
                cache.put("" + i, 0);
            } else {
                // 当key不存在时，会立即创建出对象来返回，age2不会为空
                Integer age2 = cache.get(i + "", k -> {
                    System.out.println("默认值:" + k);
                    return 0;
                });
                cache.put("" + i, age2 + 1);
            }
        }
        log.info("cache2 :{}", cache.asMap());

        System.out.println(cache.getIfPresent(keyName));
    }


    @GetMapping(value = "/getValueManualCountByConfig")
    public void getValueFromCaffeineManualByConfig(@RequestParam(value = "key") String keyName) {

        Integer age1 = (Integer) cache.getIfPresent(keyName);
        System.out.println(age1);

        for (int i = 0; i < 10; i++) {
            @Nullable Integer count = (Integer) cache.getIfPresent(i + "");
            if (null == count) {
                cache.put("" + i, 0);
            }
        }
        log.info("cache1 :{}", cache.asMap());

        for (int i = 4; i < 11; i++) {
            @Nullable Integer count = (Integer) cache.getIfPresent(i + "");
            if (null == count) {
                cache.put("" + i, 0);
            } else {
                // 当key不存在时，会立即创建出对象来返回，age2不会为空
                Integer age2 = (Integer) cache.get(i + "", k -> {
                    System.out.println("默认值:" + k);
                    return 0;
                });
                cache.put("" + i, age2 + 1);
            }
        }
        log.info("cache2 :{}", cache.asMap());

        System.out.println(cache.getIfPresent(keyName));
    }

    @GetMapping(value = "/testGet")
    public void getTest(@RequestParam(value = "key") String keyName) {
        Object age1 = cache.get(keyName, value -> 0);
        System.out.println("value:" + age1);

        log.info("cache config :{}", cache.asMap());

    }

    @GetMapping(value = "/testPut")
    public void putTest(@RequestParam(value = "key") String keyName, @RequestParam(value = "value") String valueName) {
        cacheService.put(keyName, valueName);

    }

}
