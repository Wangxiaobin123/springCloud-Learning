package k8s.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import k8s.service.k8s.K8sModelReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * K8S API Operation
 * <p>
 * 后续:
 * 可以动态传值,详情参考registry_deployment文件
 *
 * </p>
 *
 * @author: wangshengbin
 * @date: 2020/7/7 上午10:47
 */
@Api(description = "发布等操作")
@RestController
@RequestMapping(value = "/mfAi/k8s")
public class K8sOperationController {

    @Autowired
    private K8sModelReleaseService k8sModelReleaseService;

    @ApiOperation(value = "模型发布", notes = "\n" +
            "<table summary=\"Response Details\" border=\"1\">\n" +
            "        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>\n" +
            "        <tr><td> 200 </td><td> OK </td><td>  -  </td></tr>\n" +
            "        <tr><td> 201 </td><td> Created </td><td>  -  </td></tr>\n" +
            "        <tr><td> 202 </td><td> Accepted </td><td>  -  </td></tr>\n" +
            "        <tr><td> 401 </td><td> Unauthorized </td><td>  -  </td></tr>\n" +
            "        <tr><td> 409 </td><td> Conflict </td><td>  -  </td></tr>\n" +
            "     </table>")
    @PostMapping(value = "/model/release")
    public int releaseModel() {
        String yml = "apiVersion: machinelearning.seldon.io/v1\n" +
                "kind: SeldonDeployment\n" +
                "metadata:\n" +
                "  name: seldon-deployment-example-v120\n" +
                "  namespace: seldon\n" +
                "spec:\n" +
                "  name: sklearn-iris-deployment\n" +
                "  predictors:\n" +
                "  - componentSpecs:\n" +
                "    - spec:\n" +
                "        nodeName: master1\n" +
                "        containers:\n" +
                "        - image: albert-classifier:2.0\n" +
                "          imagePullPolicy: IfNotPresent\n" +
                "          name: sklearn-iris-classifier\n" +
                "          env:\n" +
                "          - name: MODEL_PATH\n" +
                "            value: \"/mnt/ai_platform_models/ckpt/1594103091\"\n" +
                "          volumeMounts:\n" +
                "          - name: model-share\n" +
                "            mountPath: \"/mnt\"\n" +
                "            readOnly: true\n" +
                "        volumes:\n" +
                "        - name: model-share\n" +
                "          nfs:\n" +
                "            server: 172.24.7.167\n" +
                "            path: \"/opt/nfs_file\"\n" +
                "    graph:\n" +
                "      children: []\n" +
                "      endpoint:\n" +
                "        type: REST\n" +
                "      name: sklearn-iris-classifier\n" +
                "      type: MODEL\n" +
                "    name: sklearn-iris-predictor\n" +
                "    replicas: 1\n";
        return k8sModelReleaseService.releaseModel(yml);
    }
}
