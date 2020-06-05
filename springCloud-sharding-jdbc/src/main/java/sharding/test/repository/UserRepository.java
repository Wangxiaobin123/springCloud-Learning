package sharding.test.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sharding.test.entity.User;

/**
 * @author: wangshengbin
 * @date: 2020/6/4 下午6:42
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * find user by Name
     *
     * @param name
     * @return
     */
    User findByName(String name);
}
