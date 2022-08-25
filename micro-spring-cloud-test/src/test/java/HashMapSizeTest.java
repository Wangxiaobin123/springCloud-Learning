import micro.spring.cloud.test.util.InitSizeUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wangshengbin
 * @date: 2020/8/17 上午10:22
 */
public class HashMapSizeTest {
    public static final int SIZE = 10000000;
    @Test
    public void testHashMapSize() {
        long t1 = System.currentTimeMillis();
        Map<String, String> mapDefault = new HashMap<>(16);
        for (int i = 0; i < SIZE; i++) {
            mapDefault.put(i + "", i + "");
        }
        System.out.println((System.currentTimeMillis() - t1) + ",size = " + mapDefault.size());

        long t2 = System.currentTimeMillis();
        Map<String, String> mapInitSize = new HashMap<>(InitSizeUtil.initHashMapsize(SIZE));
        for (int i = 0; i < SIZE; i++) {
            mapInitSize.put(i + "", i + "");
        }
        System.out.println((System.currentTimeMillis() - t2) + ",size = " + mapInitSize.size());

        long t3 = System.currentTimeMillis();
        Map<String, String> mapInitSizeOfNum = new HashMap<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            mapInitSizeOfNum.put(i + "", i + "");
        }
        System.out.println((System.currentTimeMillis() - t3) + ",size = " + mapInitSizeOfNum.size());

    }
}
