package algorithm.synchro;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: wangshengbin
 * @date: 2020/12/27 下午10:08
 */

public class VolatileTest {

    private static volatile double MY_INT = 0d;

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 100; i++) {
            es.submit(new Runnable() {
                public void run() {
                    System.out.println(MY_INT++);
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                ;
            });
        }
        es.shutdown();
    }
}