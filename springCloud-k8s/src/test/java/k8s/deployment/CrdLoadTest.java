package k8s.deployment;

import cn.hutool.core.io.file.FileReader;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionList;
import io.fabric8.kubernetes.client.KubernetesClient;
import k8s.util.InitClientTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: wangshengbin
 * @date: 2020/7/10 上午11:34
 */
public class CrdLoadTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrdLoadTest.class);
    private static final String SELDON_CONFIG = "seldon.yml";


    @Test
    public void testCrd() throws IOException {

        try (final KubernetesClient client = InitClientTest.getClient()) {
            // List all Custom resources.
            log("Listing all current Custom Resource Definitions :");
            CustomResourceDefinitionList crdList = client.customResourceDefinitions().list();
            crdList.getItems().forEach(crd -> log(crd.getMetadata().getName()));


            String seldonConfigPath = Objects.requireNonNull(Fabric8ApiTest.class.getClassLoader().getResource(SELDON_CONFIG)).getPath();
            FileReader fileReader;
            fileReader = new FileReader(seldonConfigPath);

            // Creating a custom resource from yaml
            CustomResourceDefinition aCustomResourceDefinition = client.customResourceDefinitions().load(fileReader.getInputStream()).get();
            log("Creating CRD...");
            client.customResourceDefinitions().create(aCustomResourceDefinition);

            log("Updated Custom Resource Definitions: ");
            client.customResourceDefinitions().list().getItems().forEach(crd -> log(crd.getMetadata().getName()));

        }
    }

    private static void log(String action, Object obj) {
        LOGGER.info("{}: {}", action, obj);
    }

    private static void log(String action) {
        LOGGER.info(action);
    }
}
