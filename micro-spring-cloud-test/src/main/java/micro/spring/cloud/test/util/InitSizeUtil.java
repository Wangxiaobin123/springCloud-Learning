package micro.spring.cloud.test.util;

/**
 * @author: wangshengbin
 * @date: 2020/8/17 上午10:12
 */
public class InitSizeUtil {

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    public static int initHashMapsize(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
