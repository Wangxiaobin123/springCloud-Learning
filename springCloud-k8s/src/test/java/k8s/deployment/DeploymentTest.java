package k8s.deployment;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Yaml;
import k8s.KubeConfigFileClientExample;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * @author: wangshengbin
 * @date: 2020/6/30 下午11:16
 */
public class DeploymentTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentTest.class);
    private static final String K8S_CONFIG = "k8s_config";

    /**
     * 创建空间
     */
    @Test
    public void testCreateNameSpace() {
        String k8sConfigPath = Objects.requireNonNull(DeploymentTest.class.getClassLoader().getResource(K8S_CONFIG)).getPath();
        ApiClient client = null;
        try {
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(k8sConfigPath))).build();
        } catch (IOException e) {
            LOGGER.error("Error init client:", e);
        }
        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        CoreV1Api apiInstance = new CoreV1Api(client);

        // 参数
        V1Namespace body = new V1Namespace();

        V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
        v1ObjectMeta.setName("shengbin-test112");

        body.setMetadata(v1ObjectMeta);
        body.setApiVersion("v1");
        body.setKind("Namespace");

        try {
            V1Namespace result = apiInstance.createNamespace(body, null, null, null);
            System.out.println(result);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除空间
     */
    @Test
    public void testDeleteNameSpace1() {
        String k8sConfigPath = Objects.requireNonNull(DeploymentTest.class.getClassLoader().getResource(K8S_CONFIG)).getPath();
        ApiClient client = null;
        try {
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(k8sConfigPath))).build();
        } catch (IOException e) {
            LOGGER.error("Error init client:", e);
        }
        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        CoreV1Api apiInstance = new CoreV1Api(client);
        // String | name of the Namespace
        String name = "shengbin-test112";
        // Integer | The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
        Integer gracePeriodSeconds = 0;
        try {
            V1Status result = apiInstance.deleteNamespace(name, null, null, gracePeriodSeconds, null, null, null);
            System.out.println(result);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    /**
     * 部署nginx
     * <p>
     * k8s.kuboard.cn/layer
     * <p>
     * web 展现层
     * gateway 网关层
     * svc 微服务层
     * db 持久层
     * monitor 监控
     * cloud 中间件
     * </p>
     */
    @Test
    public void testCreateDeployments() {
        String k8sConfigPath = Objects.requireNonNull(DeploymentTest.class.getClassLoader().getResource(K8S_CONFIG)).getPath();
        ApiClient client = null;
        try {
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(k8sConfigPath))).build();
        } catch (IOException e) {
            LOGGER.error("Error init client:", e);
        }
        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        AppsV1Api apiInstance = new AppsV1Api(client);

        V1Deployment deployment = new V1DeploymentBuilder()
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withNewMetadata()
                .addToLabels("k8s.kuboard.cn/layer", "web")
                .withName("nginx-deployment")
                .endMetadata()
                .withNewSpec()
                .withNewSelector()
                .addToMatchLabels("app", "nginx")
                .endSelector()
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", "nginx")
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withImage("nginx:1.7.9")
                .withName("nginx")
                .addNewPort()
                .withContainerPort(80)
                .endPort()
                .endContainer()
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();
        System.out.println(Yaml.dump(deployment));
        // String | object name and auth scope, such as for teams and projects
        String namespace = "shengbin-test112";

        try {
            V1Deployment result = apiInstance.createNamespacedDeployment(namespace, deployment, null, null, null);
            System.out.println(result);
        } catch (ApiException e) {

            e.printStackTrace();
        }
    }

    /**
     * 部署nginx
     * <p>
     * k8s.kuboard.cn/layer
     * <p>
     * web 展现层
     * gateway 网关层
     * svc 微服务层
     * db 持久层
     * monitor 监控
     * cloud 中间件
     * </p>
     */
    @Test
    public void testCreateDeploymentsByYml() {
        String k8sConfigPath = Objects.requireNonNull(DeploymentTest.class.getClassLoader().getResource(K8S_CONFIG)).getPath();
        String yml = Objects.requireNonNull(Configuration.class.getClassLoader().getResource("nginx.yml")).getPath();
        File file = new File(yml);
        V1Deployment deployment = null;
        try {
            Object load = Yaml.load(file);
            if (load instanceof V1Deployment) {
                deployment = (V1Deployment) load;
                System.out.println(Yaml.dump(deployment));
            } else {
                System.err.println("Error");
            }
        } catch (IOException e) {
            LOGGER.error("Error load file:", e);
        }
        ApiClient client = null;
        try {
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(k8sConfigPath))).build();
        } catch (IOException e) {
            LOGGER.error("Error init client:", e);
        }
        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        AppsV1Api apiInstance = new AppsV1Api(client);
        String namespace = "shengbin-test112";

        try {
            V1Deployment result = apiInstance.createNamespacedDeployment(namespace, deployment, null, null, null);
            System.out.println(result);
        } catch (ApiException e) {

            e.printStackTrace();
        }
    }


    /**
     * 部署 spring registry
     * <p>
     * //apiVersion: apps/v1
     *                 //kind: Deployment
     *                 //metadata:
     *                 //  namespace: shengbin-test112
     *                 //  name: svc-registry-deployment-01
     *                 //  annotations:
     *                 //    k8s.kuboard.cn/workload: svc-registry-deployment-01
     *                 //    k8s.kuboard.cn/displayName: 注册中心
     *                 //    k8s.kuboard.cn/ingress: 'false'
     *                 //    k8s.kuboard.cn/service: NodePort
     *                 //  labels:
     *                 //    k8s.kuboard.cn/layer: svc
     *                 //    k8s.kuboard.cn/name: svc-registry-deployment-01
     *                 //spec:
     *                 //  selector:
     *                 //    matchLabels:
     *                 //      k8s.kuboard.cn/layer: svc
     *                 //      k8s.kuboard.cn/name: svc-registry-deployment-01
     *                 //  revisionHistoryLimit: 10
     *                 //  template:
     *                 //    metadata:
     *                 //      labels:
     *                 //        k8s.kuboard.cn/layer: svc
     *                 //        k8s.kuboard.cn/name: svc-registry-deployment-01
     *                 //    spec:
     *                 //      securityContext:
     *                 //        seLinuxOptions: {}
     *                 //      imagePullSecrets: []
     *                 //      restartPolicy: Always
     *                 //      initContainers: []
     *                 //      containers:
     *                 //        - image: 'wang921217/mf-spring-cloud-registry:1.0'
     *                 //          imagePullPolicy: IfNotPresent
     *                 //          name: registry
     *                 //          volumeMounts:
     *                 //            - name: config
     *                 //              mountPath: /opt/app-root/src
     *                 //              mountPropagation: HostToContainer
     *                 //              readOnly: false
     *                 //          args:
     *                 //            - >-
     *                 //              --spring.profiles.active=pro1,--spring.config.location=/opt/app-root/src/application-pro1.properties
     *                 //          resources:
     *                 //            limits: {}
     *                 //            requests: {}
     *                 //          env: []
     *                 //          lifecycle: {}
     *                 //      volumes:
     *                 //        - name: config
     *                 //          nfs:
     *                 //            server: 172.24.7.167
     *                 //            path: /opt/nfs_file/
     *                 //      dnsPolicy: ClusterFirst
     *                 //      dnsConfig: {}
     *                 //      terminationGracePeriodSeconds: 30
     *                 //  progressDeadlineSeconds: 600
     *                 //  strategy:
     *                 //    type: RollingUpdate
     *                 //    rollingUpdate:
     *                 //      maxUnavailable: 25%
     *                 //      maxSurge: 25%
     *                 //  replicas: 1
     * </p>
     * <p>
     * k8s.kuboard.cn/layer
     * <p>
     * web 展现层
     * gateway 网关层
     * svc 微服务层
     * db 持久层
     * monitor 监控
     * cloud 中间件
     * </p>
     */
    @Test
    public void testCreateRegistryDeployments() {
        String k8sConfigPath = Objects.requireNonNull(DeploymentTest.class.getClassLoader().getResource(K8S_CONFIG)).getPath();
        ApiClient client = null;
        try {
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(k8sConfigPath))).build();
        } catch (IOException e) {
            LOGGER.error("Error init client:", e);
        }
        Configuration.setDefaultApiClient(client);
        V1Deployment deployment = new V1DeploymentBuilder()
                .withApiVersion("apps/v1")
                .withKind("Deployment")
                .withNewMetadata()
                    .withNamespace("shengbin-test112")
                    .withName("svc-registry-deployment-01")
                    .addToAnnotations("k8s.kuboard.cn/workload", "svc-registry-deployment-01")
                    .addToAnnotations("k8s.kuboard.cn/displayName", "注册中心")
                    .addToAnnotations("k8s.kuboard.cn/service", "NodePort")
                    .addToAnnotations("k8s.kuboard.cn/ingress", "false")
                    .addToLabels("k8s.kuboard.cn/layer", "svc")
                    .addToLabels("k8s.kuboard.cn/name", "svc-registry-deployment-01")
                .endMetadata()
                .withNewSpec()
                    .withNewSelector()
                        .addToMatchLabels("k8s.kuboard.cn/layer", "svc")
                        .addToMatchLabels("k8s.kuboard.cn/name", "svc-registry-deployment-01")
                    .endSelector()
                    .withRevisionHistoryLimit(10)
                .withNewTemplate()
                    .withNewMetadata()
                        .addToLabels("k8s.kuboard.cn/layer", "svc")
                        .addToLabels("k8s.kuboard.cn/name", "svc-registry-deployment")
                    .endMetadata()
                    .withNewSpec()
                        .withNewSecurityContext()
                            .withNewSeLinuxOptions()
                            .endSeLinuxOptions()
                        .endSecurityContext()
                        .withRestartPolicy("Always")
                        .addNewContainer()
                            .withImage("wang921217/mf-spring-cloud-registry:1.0")
                            .withName("registry")
                            .withImagePullPolicy("IfNotPresent")
                            .addNewVolumeMount()
                                .withName("config")
                                .withMountPath("/opt/app-root/src")
                                .withMountPropagation("HostToContainer")
                                .withReadOnly(false)
                            .endVolumeMount()
                            .addNewArg("--spring.profiles.active=pro2,--spring.config.location=/opt/app-root/src/application-pro2.properties")
                            .withNewResources()
                            .endResources()
                        .endContainer()
                        .addNewVolume()
                            .withName("config")
                            .withNewNfs()
                                .withServer("172.24.7.167")
                                .withPath("/opt/nfs_file/")
                            .endNfs()
                        .endVolume()
                        .withDnsPolicy("ClusterFirst")
                        .withTerminationGracePeriodSeconds(30L)
                    .endSpec()
                .endTemplate()
                .withProgressDeadlineSeconds(600)
                .withReplicas(1)
                .endSpec()
                .build();
        AppsV1Api apiInstance = new AppsV1Api(client);
        String namespace = "shengbin-test112";
        try {
            V1Deployment result = apiInstance.createNamespacedDeployment(namespace, deployment, null, null, null);
            System.out.println(result);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 部署 registry
     * <p>
     * k8s.kuboard.cn/layer
     * <p>
     * web 展现层
     * gateway 网关层
     * svc 微服务层
     * db 持久层
     * monitor 监控
     * cloud 中间件
     * </p>
     * <p>
     * service:
     * ClusterIP
     * NodePort
     * </p>
     */
    @Test
    public void testCreateRegistryDeploymentsByYml() {
        String k8sConfigPath = Objects.requireNonNull(DeploymentTest.class.getClassLoader().getResource(K8S_CONFIG)).getPath();
        String yml = Objects.requireNonNull(Configuration.class.getClassLoader().getResource("registry_deployment.yml")).getPath();
        File file = new File(yml);
        V1Deployment deployment = null;
        try {
            Object load = Yaml.load(file);
            if (load instanceof V1Deployment) {
                deployment = (V1Deployment) load;
                LOGGER.info("yml:{}", Yaml.dump(deployment));
            } else {
                System.err.println("Error");
            }
        } catch (IOException e) {
            LOGGER.error("Error load file:", e);
        }
        ApiClient client = null;
        try {
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(k8sConfigPath))).build();
        } catch (IOException e) {
            LOGGER.error("Error init client:", e);
        }
        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        AppsV1Api apiInstance = new AppsV1Api(client);
        String namespace = "shengbin-test112";

        try {
            V1Deployment result = apiInstance.createNamespacedDeployment(namespace, deployment, null, null, null);
            System.out.println(result);
        } catch (ApiException e) {

            e.printStackTrace();
        }
    }

    /**
     * 创建 service
     */
    @Test
    public void testCreateService() {
        String k8sConfigPath = Objects.requireNonNull(DeploymentTest.class.getClassLoader().getResource(K8S_CONFIG)).getPath();
        String yml = Objects.requireNonNull(Configuration.class.getClassLoader().getResource("registry_service.yml")).getPath();
        File file = new File(yml);
        V1Service v1Service = null;

        ApiClient client = null;
        try {
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(k8sConfigPath))).build();
        } catch (IOException e) {
            LOGGER.error("Error init client:", e);
        }
        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        CoreV1Api apiInstance = new CoreV1Api(client);
        String namespace = "shengbin-test112";

        try {
            V1Service result = apiInstance.createNamespacedService(namespace, v1Service, null, null, null);
            System.out.println(result);
        } catch (ApiException e) {

            e.printStackTrace();
        }
    }

    @Test
    public void testCreateServiceByYml() {
        String k8sConfigPath = Objects.requireNonNull(DeploymentTest.class.getClassLoader().getResource(K8S_CONFIG)).getPath();
        String yml = Objects.requireNonNull(Configuration.class.getClassLoader().getResource("registry_service.yml")).getPath();
        File file = new File(yml);
        V1Service v1Service = null;
        try {
            Object load = Yaml.load(file);
            if (load instanceof V1Service) {
                v1Service = (V1Service) load;
                LOGGER.info("yml to service:{}", Yaml.dump(v1Service));
            } else {
                System.err.println("Error");
            }
        } catch (IOException e) {
            LOGGER.error("Error load file:", e);
        }
        ApiClient client = null;
        try {
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(k8sConfigPath))).build();
        } catch (IOException e) {
            LOGGER.error("Error init client:", e);
        }
        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        CoreV1Api apiInstance = new CoreV1Api(client);
        String namespace = "shengbin-test112";

        try {
            V1Service result = apiInstance.createNamespacedService(namespace, v1Service, null, null, null);
            System.out.println(result);
        } catch (ApiException e) {

            e.printStackTrace();
        }
    }

    @Test
    public void testYmlToObject() {
        String k8sConfigPath = Objects.requireNonNull(Configuration.class.getClassLoader().getResource("nginx.yml")).getPath();
        File file = new File(k8sConfigPath);
        try {
            Object load = Yaml.load(file);
            if (load instanceof V1Deployment) {
                V1Deployment deployment = (V1Deployment) load;
                System.out.println(Yaml.dump(deployment));
            } else {
                System.err.println("Error");
            }
        } catch (IOException e) {
            LOGGER.error("Error load file:", e);
        }
    }
}
