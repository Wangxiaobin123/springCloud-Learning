package cloud.SynchUser;

import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

/**
 * @Author: shengbin
 * @since: 2019/7/13 下午11:16
 */
public class SynchroTest extends HystrixObservableCommand<SynchUser> {
    private Long id;

    protected SynchroTest(Setter setter, Long id) {
        super(setter);
        this.id = id;
    }

    @Override
    protected Observable<SynchUser> construct() {
        return Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    SynchUser synchUser = new SynchUser();
                    synchUser.setUserId(id);
                    subscriber.onNext(synchUser);
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }

        });
    }
}
