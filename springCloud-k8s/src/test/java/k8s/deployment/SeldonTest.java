package k8s.deployment;

import org.junit.Test;

/**
 * @author: wangshengbin
 * @date: 2020/7/8 下午12:35
 */
public class SeldonTest {
    @Test
    public void testSeldon() {

        Integer a = new Integer(1);
        Integer b = new Integer(1);

        // false
        System.out.println("a==b? " + (a==b));

        Integer c = Integer.valueOf(127);
        Integer d = Integer.valueOf(127);

        // true
        System.out.println("c==d? " + (c==d));

        Integer e = Integer.valueOf(128);
        Integer f = Integer.valueOf(128);
        // false
        System.out.println("e==f? " + (e==f));
    }
}

