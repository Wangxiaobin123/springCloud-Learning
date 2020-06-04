package test.apollo.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author: wangshengbin
 * @date: 2020/3/28 下午11:23
 */
@Configuration
@EnableApolloConfig({"application","mf_pro.yml"})
public class ApolloAutoConfig {

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${str1}")
    private String str1;

    @Value("${bfd.system.redis-config.addr}")
    private String redisConfigAddr;

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getRedisConfigAddr() {
        return redisConfigAddr;
    }

    public void setRedisConfigAddr(String redisConfigAddr) {
        this.redisConfigAddr = redisConfigAddr;
    }


}
