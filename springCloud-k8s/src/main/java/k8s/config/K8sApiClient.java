package k8s.config;

import cn.hutool.core.io.file.FileReader;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.kubernetes.client.openapi.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: wangshengbin
 * @date: 2020/7/7 下午4:39
 */
@Configuration
public class K8sApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(K8sApiClient.class);

    @Bean
    public KubernetesClient apiClient() throws IOException {
        String k8sConfigPath = Objects.requireNonNull(ApiClient.class.getClassLoader().getResource("k8s_config")).getPath();
        KubernetesClient client;
        FileReader fileReader;
        String result;
        Config config;
        try {
            fileReader = new FileReader(k8sConfigPath);
            result = fileReader.readString();
            config = Config.fromKubeconfig(result);
            client = new DefaultKubernetesClient(config);
        } catch (Exception e) {
            fileReader = new FileReader(System.getProperty("user.dir") + "/config/" +"k8s_config");
            result = fileReader.readString();
            config = Config.fromKubeconfig(result);
            client = new DefaultKubernetesClient(config);

        }
        LOGGER.info("Init k8s client ok!");
        return client;
    }
}
