package algorithm.compare;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author: wangshengbin
 * @date: 2021/2/4 下午2:50
 */
public class TestObject3 {
    Integer a;
    public TestObject3(Integer value){
        this.a =  value;
    }

    @Override
    public int hashCode() {
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject3 that = (TestObject3) o;
        return Objects.equals(a, that.a);
    }

    public static void main(String[] args){
        TestObject3 a = new TestObject3(1);
        TestObject3 a1 = new TestObject3(1);

        // false
        System.out.println(a==a1);
        // true
        System.out.println(a.equals(a1));


        HashMap<TestObject3,String> map = new HashMap<>(2);
        map.put(a,"v1");
        map.put(a1,"v2");

        // 1
        System.out.println(map.keySet().size());


    }
}
