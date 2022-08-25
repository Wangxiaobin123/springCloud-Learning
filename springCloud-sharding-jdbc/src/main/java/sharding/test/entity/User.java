package sharding.test.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author: wangshengbin
 * @date: 2020/6/4 下午6:40
 */
@Entity
@Table(name = "user_0")
public class User implements Serializable {
    private static final long serialVersionUID = 2353611132028725768L;
    private Long id;

    private String city = "";

    private String name = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
