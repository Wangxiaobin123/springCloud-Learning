package k8s.deployment;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wangshengbin
 * @date: 2020/7/10 下午4:29
 */
public class StrTest {


    @Test
    public void testStr() {

        String str = "apiVersion: machinelearning.seldon.io/v1\n" +
                "kind: SeldonDeployment\n" +
                "metadata:\n" +
                "  name: ${MODEl_NAME}\n" +
                "  namespace: seldon\n" +
                "spec:\n" +
                "  name: ${MODEl_NAME}-deployment\n" +
                "  predictors:\n" +
                "  - componentSpecs:\n" +
                "    - spec:\n" +
                "        nodeName: master1\n" +
                "        containers:\n" +
                "        - image: albert-classifier:2.0\n" +
                "          imagePullPolicy: IfNotPresent\n" +
                "          name: ${MODEl_NAME}-classifier\n" +
                "          env:\n" +
                "          - name: MODEL_PATH\n" +
                "            value: ${MODEL_PATH}\n" +
                "          volumeMounts:\n" +
                "          - name: model-share\n" +
                "            mountPath: /mnt\n" +
                "            readOnly: true\n" +
                "        volumes:\n" +
                "        - name: model-share\n" +
                "          nfs:\n" +
                "           server: 172.24.7.167\n" +
                "           path: /opt/nfs_file\n" +
                "    graph:\n" +
                "      children: []\n" +
                "      endpoint:\n" +
                "        type: REST\n" +
                "      name: ${MODEl_NAME}-classifier\n" +
                "      type: MODEL\n" +
                "    name: ${MODEl_NAME}-predictor\n" +
                "    replicas: 1\n";
        System.err.println(str.replaceAll("\\$\\{MODEL_PATH}", "ccccccc....."));
        //str =   templateParseWithMountPath(str,"/opt/nfs_file/dsds/dsdsds");
        // System.err.println(str);

        Yaml yaml = new Yaml();
        Map<String,Object> load = yaml.load(str);
        Map<String,Object> metadata = (Map<String, Object>) load.getOrDefault("metadata", new HashMap<String,Object>());
        System.out.println(metadata.getOrDefault("name",""));

    }
}
