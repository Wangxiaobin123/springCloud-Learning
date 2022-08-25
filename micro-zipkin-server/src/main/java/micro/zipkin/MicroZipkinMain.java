package micro.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;

/**
 * @Author: shengbin
 * @since: 2019/4/30 下午4:44
 */
@EnableZipkinServer
//@EnableZipkinStreamServer
@SpringBootApplication
public class MicroZipkinMain {
    public static void main(String[] args) {
        SpringApplication.run(MicroZipkinMain.class, args);
    }
}
