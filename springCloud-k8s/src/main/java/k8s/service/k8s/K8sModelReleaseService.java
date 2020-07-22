package k8s.service.k8s;

import cn.hutool.core.util.StrUtil;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DoneableDeployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import io.fabric8.kubernetes.client.internal.readiness.Readiness;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;
import k8s.domain.CustomResourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: wangshengbin
 * @date: 2020/7/7 下午5:08
 */
@Service
public class K8sModelReleaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(K8sModelReleaseService.class);
    private static final String META_DATA = "metadata";
    private static final String NAME = "name";
    private static final String NAME_SPACE = "namespace";
    @Autowired
    private KubernetesClient kubernetesClient;
    @Autowired
    private WatcherService watcherService;
    @Autowired
    private DeployService deployService;
    @Autowired
    private KubernetesClientService kubernetesClientService;

    /**
     * 发布
     * <p>
     * commented on https://github.com/fabric8io/fabric8-maven-plugin/issues/1377 as this approach could be used to fix that
     *
     * @param yml
     * @return
     */
    public int releaseModel(String yml) {
        // 解析 yml
        Yaml yaml = new Yaml();
        Map<String, Object> load = yaml.load(yml);
        Object object = load.get(META_DATA);
        Map<String, Object> metadata = object instanceof Map ? (Map) object : null;
        String objectName = metadata != null ? (String) metadata.getOrDefault(NAME, null) : null;
        String namespace = metadata != null ? (String) metadata.getOrDefault(NAME_SPACE, null) : null;
        if (StrUtil.isEmpty(objectName)) {
            return -1;
        }
        final int[] code = {0};
        try {
            CustomResourceDefinition crd = getCrd();
            // Registers a Custom Resource Definition Kind
            KubernetesDeserializer.registerCustomKind(crd.getSpec().getGroup() + "/" + crd.getSpec().getVersion(), crd.getSpec().getNames().getKind(), CustomResourceImpl.class);
            CustomResourceImpl deployedResource = getCustomResourceObject(crd, objectName, namespace);
            CountDownLatch deleteLatch = new CountDownLatch(1);
            CountDownLatch closeLatch = new CountDownLatch(1);
            Watch watch = watcherService.createWatch(crd, objectName, deployedResource != null, deleteLatch, closeLatch, namespace);
            deployService.deploy(crd, yml, namespace);
            watcherService.waitForLatch(deleteLatch, "Failed to watch for and delete " + objectName);
            watch.close();
            watcherService.waitForLatch(closeLatch, "Failure in watch");


            CountDownLatch latch = new CountDownLatch(1);
            ExecutorService executorService = Executors.newFixedThreadPool(1);

            MixedOperation<Deployment, DeploymentList, DoneableDeployment, RollableScalableResource<Deployment, DoneableDeployment>> deployments = kubernetesClient.apps().deployments();
            NonNamespaceOperation<Deployment, DeploymentList, DoneableDeployment, RollableScalableResource<Deployment, DoneableDeployment>> seldon = deployments.inNamespace(namespace);
            DeploymentList list = seldon.list();
            for (Deployment deployment : list.getItems()) {
                if (objectName.equals(deployment.getMetadata().getLabels().getOrDefault("seldon-deployment-id", null))) {

                    executorService.submit(() -> {
                        while (true) {
                            if (Readiness.isReady(deployment)) {
                                code[0] = 0;
                                break;
                            } else {
                                code[0] = -1;
                            }
                        }
                    });

                }
            }
            try {
                latch.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        } catch (Exception e) {
            LOGGER.error("Error releaseModel:", e);
            code[0] = -1;
        }
        return code[0];
    }

    /**
     * 下线模型
     *
     * @param yml
     * @return
     */
    public int deleteModel(String yml) {
        // 解析 yml
        Yaml yaml = new Yaml();
        Map<String, Object> load = yaml.load(yml);
        Object object = load.get(META_DATA);
        Map<String, Object> metadata = object instanceof Map ? (Map) object : null;
        String objectName = metadata != null ? (String) metadata.getOrDefault(NAME, null) : null;
        String namespace = metadata != null ? (String) metadata.getOrDefault(NAME_SPACE, null) : null;
        if (StrUtil.isEmpty(objectName)) {
            return -1;
        }
        int code = 0;
        try {
            MixedOperation<Deployment, DeploymentList, DoneableDeployment, RollableScalableResource<Deployment, DoneableDeployment>> deployments = kubernetesClient.apps().deployments();
            NonNamespaceOperation<Deployment, DeploymentList, DoneableDeployment, RollableScalableResource<Deployment, DoneableDeployment>> seldon = deployments.inNamespace(namespace);
            DeploymentList list = seldon.list();
            for (Deployment deployment : list.getItems()) {
                if (objectName.equals(deployment.getMetadata().getLabels().getOrDefault("seldon-deployment-id", null))) {
                    Boolean delete = seldon.delete(deployment);
                    LOGGER.info("delete result :{},deployment :{}", delete, deployment);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error deleteModel:", e);
            code = -1;
        }
        return code;
    }


    /**
     * k8s CRD information
     *
     * @return
     * @throws Exception
     */
    private CustomResourceDefinition getCrd() throws Exception {
        CustomResourceDefinition customResourceDefinition = kubernetesClient.customResourceDefinitions().withName("seldondeployments.machinelearning.seldon.io").get();
        if (customResourceDefinition == null) {
            // note: nameSpace seldon must be created before deploy CRD of seldonDeployment
            throw new Exception("K8s CRD " + "seldondeployments.machinelearning.seldon.io" + " not present,it is must be exist!");
        }
        return customResourceDefinition;
    }

    private CustomResourceImpl getCustomResourceObject(CustomResourceDefinition crd, String objectName, String namespace) {
        LOGGER.info("Create CRD:{}", crd);
        return kubernetesClientService.createCrdClient(crd, namespace).withName(objectName).get();
    }


}
