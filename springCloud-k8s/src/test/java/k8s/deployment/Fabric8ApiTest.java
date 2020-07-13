package k8s.deployment;

import cn.hutool.core.io.file.FileReader;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.dsl.internal.RawCustomResourceOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.RollingOperationContext;
import k8s.util.InitClientTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: wangshengbin
 * @date: 2020/7/8 上午10:41
 */
public class Fabric8ApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Fabric8ApiTest.class);
    private static final String K8S_CONFIG = "k8s_config";
    private static final String SELDON_CONFIG = "seldon.yml";


    @Test
    public void testCreateRegistryDeploymentsByYml() throws IOException {

        String seldonConfigPath = Objects.requireNonNull(Fabric8ApiTest.class.getClassLoader().getResource(SELDON_CONFIG)).getPath();
        FileReader fileReader;

        KubernetesClient client = InitClientTest.getClient();

        fileReader = new FileReader(seldonConfigPath);
        RawCustomResourceOperationsImpl rawCustomResourceOperations = client.customResource(seldonCrdCxt());
        rawCustomResourceOperations.createOrReplace(fileReader.getInputStream());

        // List<HasMetadata> orReplace = client.load(fileReader.getInputStream()).createOrReplace();

        LOGGER.info("\nnameSpaces:{},\nnodes:{}", client.namespaces().list(), client.nodes().list());
    }


    public RollingOperationContext deploment() {
        RollingOperationContext context = new RollingOperationContext();
        context.withApiGroupName("apps")
                .withApiGroupVersion("v1")
                .withPlural("deployments");
        return context;
    }

    public CustomResourceDefinitionContext seldonCrdCxt() {
        return new CustomResourceDefinitionContext
                .Builder()
                .withGroup("machinelearning.seldon.io")
                .withScope("Namespaced")
                .withVersion("v1")
                .withName("seldondeployments")
                .withPlural("seldondeployments")
                .build();
    }

    @Test
    public void testJava() {
        Integer z = 129;
        Integer k = 129;
        System.out.println(z.equals(k));
    }


}
