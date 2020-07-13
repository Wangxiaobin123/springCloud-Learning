package k8s.service.k8s;

import cn.hutool.core.util.StrUtil;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
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
 */
@Service
public class KubernetesClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesClientService.class);

    @Autowired
    private KubernetesClient kubernetesClient;

    public NonNamespaceOperation<CustomResourceImpl, CustomResourceImplList, DoneableCustomResourceImpl, Resource<CustomResourceImpl, DoneableCustomResourceImpl>> createCrdClient(CustomResourceDefinition crd, String nameSpace) {
        NonNamespaceOperation<CustomResourceImpl, CustomResourceImplList, DoneableCustomResourceImpl, Resource<CustomResourceImpl, DoneableCustomResourceImpl>>
                crdClient = kubernetesClient.customResources(crd, CustomResourceImpl.class, CustomResourceImplList.class, DoneableCustomResourceImpl.class);

        if (StrUtil.isNotEmpty(nameSpace)) {
            crdClient = ((MixedOperation<CustomResourceImpl, CustomResourceImplList, DoneableCustomResourceImpl, Resource<CustomResourceImpl, DoneableCustomResourceImpl>>) crdClient).inNamespace(nameSpace);
        } else {
            LOGGER.warn("namespace {} is null, will be default!!!", nameSpace);
        }
        return crdClient;
    }


}
