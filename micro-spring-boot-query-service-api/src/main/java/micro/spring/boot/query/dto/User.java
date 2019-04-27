package micro.spring.boot.query.dto;

import java.io.Serializable;

/**
 * @Author: shengbin
 * @since: 2019/4/26 上午12:47
 */
public class User implements Serializable {

    private static final long serialVersionUID = -8493532688827901287L;

    private String name;
    private Integer age;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
