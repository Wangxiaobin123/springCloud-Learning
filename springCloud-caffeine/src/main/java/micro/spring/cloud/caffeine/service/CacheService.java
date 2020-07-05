package micro.spring.cloud.caffeine.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: shengbin
 * @since: 2019/11/21 下午6:38
 */
@Service
public class CacheService {
    @Autowired
    private Cache cache;

    public void put(Object key, Object value) {
        cache.put(key, value);
    }
}
