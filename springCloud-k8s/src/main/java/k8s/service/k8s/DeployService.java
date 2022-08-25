package k8s.service.k8s;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;

/**
 * @author wangshengbin
 */
public interface DeployService {

    /**
     * deploy CRD
     *
     * @param crd
     * @param yml
     * @param namespace
     * @throws Exception
     */
    void deploy(CustomResourceDefinition crd, String yml, String namespace) throws Exception;

}
