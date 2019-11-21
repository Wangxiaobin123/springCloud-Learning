import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import micro.spring.cloud.caffeine.entity.DataObject;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertNotNull;

/**
 * @Author: shengbin
 * @since: 2019/11/21 下午2:44
 */
@Slf4j
public class TestCaffeine {
    @Test
    public void test() {
        Cache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();

        String key = "A";
        DataObject dataObject = cache.getIfPresent(key);
        log.info("dataObject 1 - >{}", dataObject);
        assertNotNull(dataObject);
        log.info("dataObject 2 - >{}", dataObject);

    }

    @Test
    public void test1() {
        Cache<String, String> cache = Caffeine.newBuilder()
                //5分钟没有读写自动删除
                .expireAfterAccess(5, TimeUnit.SECONDS)
                //最大容量1024个，超过会自动清理空间
                .maximumSize(10)
                .removalListener(((key, value, cause) -> {
                    // 清理通知 key,value ==> 键值对   cause ==> 清理原因
                    System.out.println("key = "+key+",value = "+value+",cause = "+cause);
                }))
                .build();

        //添加值
        cache.put("张三", "浙江");
        //获取值
        cache.getIfPresent("张三");
        //remove
        cache.invalidate("张三");

    }

    /**
     * 手动(Manual)
     */
    @Test
    public void test2() {
        Cache<String, Integer> cache = Caffeine.newBuilder().build();

        Integer age1 = cache.getIfPresent("张三");
        System.out.println(age1);

        // 当key不存在时，会立即创建出对象来返回，age2不会为空
        Integer age2 = cache.get("张三", k -> {
            System.out.println("k:" + k);
            return 18;
        });
        System.out.println(age2);

    }

    /**
     * 自动(Loading)
     */
    @Test
    public void test3() {
        // 此时的类型是 LoadingCache 不是 Cache
        LoadingCache<String, Integer> cache = Caffeine.newBuilder().build(key -> {
            System.out.println("自动填充:" + key);
            return 18;
        });

        Integer age1 = cache.getIfPresent("张三");
        System.out.println(age1);

        // key 不存在时 会根据给定的CacheLoader自动装载进去
        Integer age2 = cache.get("张三");
        System.out.println(age2);
        Integer age3 = cache.get("张");
        System.out.println(age3);
        age3 = cache.get("王");
        System.out.println(age3);


    }

    /**
     * 异步手动(Asynchronous Manual)
     */
    @Test
    public void test4() {
        AsyncLoadingCache<String, Integer> cache = Caffeine.newBuilder().buildAsync(name -> {
            System.out.println("name:" + name);
            return 18;
        });
        CompletableFuture<Integer> ageFuture = cache.get("张三");

        Integer age = null;
        try {
            age = ageFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("age:" + age);

    }

    /**
     * 驱逐策略 - >基于容量大小:
     */
    @Test
    public void test5() {
        Cache<String, Integer> cache = Caffeine.newBuilder().maximumSize(10)
                .removalListener((key, value, cause) -> {
                    System.out.println(String.format("key %s was removed %s / %s", key, value, cause));
                }).build();

        for (int i = 0; i < 50; i++) {
            cache.put("name" + i, i);
        }
        try {
            Thread.currentThread().join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
