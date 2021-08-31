package algorithm.compare;

/**
 * @author: wangshengbin
 * @date: 2021/2/4 下午3:50
 */
public class TestInteger {
    public static void main(String[] args) {
        Integer a = new Integer(127);
        Integer b = Integer.valueOf(127);
        Integer c = Integer.valueOf(127);
        Integer d = Integer.valueOf(128);
        Integer e = Integer.valueOf(128);
        //输出false
        System.out.println(a == b);
        //输出true
        System.out.println(b == c);
        //输出false
        System.out.println(d == e);

        System.out.println("==================================");

        a = new Integer(127);
        b = Integer.valueOf(127);
        c = Integer.valueOf(127);
        d = Integer.valueOf(128);
        e = Integer.valueOf(128);
        //输出 true
        System.out.println(a.equals(b));
        //输出 true
        System.out.println(b.equals(c));
        //输出 true
        System.out.println(d.equals(e));
    }
}
