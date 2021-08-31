package algorithm.compare;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author: wangshengbin
 * @date: 2021/2/4 下午2:50
 */
public class TestObject2 {
    Integer a;
    public TestObject2(Integer value){
        this.a =  value;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            // 判断内存地址的
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        TestObject2 that = (TestObject2) o;
        return  Objects.equals(a,that.a);
    }

    public static void main(String[] args){
        TestObject2 a = new TestObject2(1);
        TestObject2 a1 = new TestObject2(1);

        // false
        System.out.println(a==a1);
        // true
        System.out.println(a.equals(a1));


        HashMap<TestObject2,String> map = new HashMap<>(2);
        map.put(a,"v1");
        map.put(a1,"v2");

        // 2
        System.out.println(map.keySet().size());


    }
}
