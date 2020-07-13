package k8s.util;

import cn.hutool.core.io.file.FileReader;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import k8s.deployment.Fabric8ApiTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: wangshengbin
 * @date: 2020/7/10 上午11:35
 */
public class InitClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitClientTest.class);
    private static final String K8S_CONFIG = "k8s_config";
    private static final String SELDON_CONFIG = "seldon.yml";

    public static KubernetesClient getClient() throws IOException {
        String k8sConfigPath = Objects.requireNonNull(Fabric8ApiTest.class.getClassLoader().getResource(K8S_CONFIG)).getPath();
        FileReader fileReader = new FileReader(k8sConfigPath);
        String result = fileReader.readString();
        Config config = Config.fromKubeconfig(result);

        return new DefaultKubernetesClient(config);

    }


}
