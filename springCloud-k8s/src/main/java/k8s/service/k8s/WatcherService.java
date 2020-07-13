package k8s.service.k8s;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.dsl.Watchable;
import k8s.domain.CustomResourceImpl;
import k8s.domain.CustomResourceImplList;
import k8s.domain.DoneableCustomResourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author wangshengbin
 */
@Service
public class WatcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatcherService.class);
    private static final long TIME_OUT = 60000;
    private static final String PRE_DELETE_CONDITION = "unknownFields['status']['state'] eq 'Available'";
    private static final Boolean DELETE_RESOURCE = true;

    @Autowired
    private KubernetesClient kubernetesClient;
    @Resource
    private DeleteService deleteService;

    public void waitForLatch(CountDownLatch deleteLatch, String message) {
        try {
            deleteLatch.await(TIME_OUT, TimeUnit.MILLISECONDS);
        } catch (KubernetesClientException | InterruptedException e) {
            LOGGER.error(message, e);
        }
    }


    public Watch createWatch(CustomResourceDefinition crd, String customResourceObjectName, boolean objectExistsAlready, CountDownLatch deleteLatch, CountDownLatch closeLatch, String namespace) {
        Watchable watchable = kubernetesClient.customResources(crd, CustomResourceImpl.class, CustomResourceImplList.class, DoneableCustomResourceImpl.class).inNamespace(namespace).withResourceVersion("0");

        if (objectExistsAlready) {
            // can just watch this obj rather than looking for it to be created
            watchable = kubernetesClient.customResources(crd, CustomResourceImpl.class, CustomResourceImplList.class, DoneableCustomResourceImpl.class).inNamespace(namespace).withName(customResourceObjectName);
        }
        return (Watch) watchable.watch(new Watcher<CustomResourceImpl>() {
            @Override
            public void eventReceived(Action action, CustomResourceImpl resource) {
                LOGGER.info("==> " + action + " for " + resource);
                if (action.equals(Action.ADDED) || action.equals(Action.MODIFIED)) {
                    if (DELETE_RESOURCE && deleteLatch.getCount() != 0) {
                        if (customResourceObjectName.equalsIgnoreCase(resource.getMetadata().getName())) {
                            if (checkExpression(resource, PRE_DELETE_CONDITION)) {
                                deleteService.delete(crd, deleteLatch, resource, customResourceObjectName, namespace);
                            }
                        } else {
                            LOGGER.info("Not deleting object " + resource.getMetadata().getName());
                        }
                    }
                }
            }

            @Override
            public void onClose(KubernetesClientException cause) {
                LOGGER.info("watch closed");
                closeLatch.countDown();
            }
        });
    }

    public boolean checkExpression(CustomResourceImpl deployedResource, String predeleteCondition) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(deployedResource);
        ExpressionParser expressionParser = new SpelExpressionParser();

        Expression expression = expressionParser.parseExpression(predeleteCondition);
        try {
            boolean result = expression.getValue(context, Boolean.class);
            LOGGER.info("{},for {},context {}", result, predeleteCondition, context);
            return result;

        } catch (SpelEvaluationException ex) {
            LOGGER.warn("Predelete expression failed:{},error:{}", predeleteCondition, ex.getMessage());
        }
        return false;
    }
}
