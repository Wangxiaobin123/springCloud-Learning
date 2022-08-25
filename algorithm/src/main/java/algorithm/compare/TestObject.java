package algorithm.compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: wangshengbin
 * @date: 2021/2/4 下午2:50
 */
public class TestObject {
    Integer a;
    public TestObject(Integer value){
        this.a =  value;
    }

    @Override
    public int hashCode() {
        return a;
    }


    public static void main(String[] args){
        List list = new ArrayList();
        list.add("a");
        TestObject a = new TestObject(1);
        TestObject a1 = new TestObject(1);

        // false
        System.out.println(a==a1);
        // false
        System.out.println(a.equals(a1));


        HashMap<TestObject,String> map = new HashMap<>(2);
        map.put(a,"v1");
        map.put(a1,"v2");

        // 2
        System.out.println(map.keySet().size());


    }
}
