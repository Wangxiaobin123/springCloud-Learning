package k8s.service.k8s;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import k8s.domain.CustomResourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

/**
 * @author wangshengbin
 */
@Service
public class Fabric8DeleteServiceImpl implements DeleteService {
    private static final Logger logger = LoggerFactory.getLogger(Fabric8DeleteServiceImpl.class);
    @Autowired
    private KubernetesClientService kubernetesClientService;

    @Override
    public void delete(CustomResourceDefinition crd,
                       CountDownLatch deleteLatch,
                       CustomResourceImpl resource,
                       String objectName,
                       String nameSpace) {

        boolean deleted = kubernetesClientService.createCrdClient(crd,nameSpace).delete(resource);
        if (deleted) {
            logger.info("deleted " + objectName);
            deleteLatch.countDown();
        } else {
            logger.error("delete via client failed");
        }
    }
}
