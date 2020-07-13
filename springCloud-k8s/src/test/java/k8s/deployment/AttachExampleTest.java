package k8s.deployment;

import com.google.common.io.ByteStreams;
import io.kubernetes.client.Attach;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

/**
 * @author: wangshengbin
 * @date: 2020/7/8 下午2:06
 */
public class AttachExampleTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachExampleTest.class);
    private static final String K8S_CONFIG = "k8s_config";

    public static void main(String[] args) throws IOException, ApiException, InterruptedException {
        String k8sConfigPath = Objects.requireNonNull(DeploymentTest.class.getClassLoader().getResource(K8S_CONFIG)).getPath();
        ApiClient client = null;
        try {
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(k8sConfigPath))).build();
        } catch (IOException e) {
            LOGGER.error("Error init client:", e);
        }
        Configuration.setDefaultApiClient(client);

        Attach attach = new Attach();
        final Attach.AttachResult result = attach.attach("shengbin-test112", "svc-registry-deployment", true);

        new Thread(
                new Runnable() {
                    public void run() {
                        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                        OutputStream output = result.getStandardInputStream();
                        try {
                            while (true) {
                                String line = in.readLine();
                                output.write(line.getBytes());
                                output.write('\n');
                                output.flush();
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                })
                .start();

        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            ByteStreams.copy(result.getStandardOutputStream(), System.out);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                })
                .start();

        Thread.sleep(10 * 1000);
        result.close();
        System.exit(0);
    }
}
