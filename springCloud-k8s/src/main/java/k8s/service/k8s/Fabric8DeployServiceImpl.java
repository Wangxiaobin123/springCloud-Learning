package k8s.service.k8s;

import cn.hutool.core.io.IoUtil;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import k8s.domain.CustomResourceImpl;
import k8s.domain.CustomResourceImplList;
import k8s.domain.DoneableCustomResourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangshengbin
 * @date: 2020/7/10 下午16:00
 */
@Service
public class Fabric8DeployServiceImpl implements DeployService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Fabric8DeployServiceImpl.class);
    @Autowired
    private KubernetesClientService kubernetesClientService;

    @Override
    public void deploy(CustomResourceDefinition crd, String yml, String nameSpace) {
        LOGGER.info("CRD yml:{}", yml);
        NonNamespaceOperation<CustomResourceImpl, CustomResourceImplList, DoneableCustomResourceImpl, Resource<CustomResourceImpl, DoneableCustomResourceImpl>>
                crdClient = kubernetesClientService.createCrdClient(crd, nameSpace);
        CustomResourceImpl resource = crdClient.load(IoUtil.toStream(yml.getBytes())).get();
        crdClient.createOrReplace(resource);
        LOGGER.info("create or replace performed on " + resource);
    }
}
