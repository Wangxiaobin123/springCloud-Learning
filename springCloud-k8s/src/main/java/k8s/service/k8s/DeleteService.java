package k8s.service.k8s;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import k8s.domain.CustomResourceImpl;

import java.util.concurrent.CountDownLatch;

/**
 * @author wangshengbin
 * @date: 2020/7/10 下午16:02
 */
public interface DeleteService {

    /**
     * delete CRD
     *
     * @param crd
     * @param deleteLatch
     * @param resource
     * @param objectName
     * @param namespace
     */
    void delete(CustomResourceDefinition crd, CountDownLatch deleteLatch, CustomResourceImpl resource, String objectName, String namespace);
}
