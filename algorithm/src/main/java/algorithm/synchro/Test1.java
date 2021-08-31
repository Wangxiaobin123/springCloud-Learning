package algorithm.synchro;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: wangshengbin
 * @date: 2021/2/19 下午4:16
 */
public class Test1 {
    private static AtomicInteger k = new AtomicInteger(0);

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int n = 0; n < 100; n++) {
                        k.incrementAndGet();
                    }
                }
            }).start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(k);
    }

    private static int t = 0;

    @Test
    public void test() {
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        synchronized (Test1.class) {
                            t++;
                        }
                    }
                }
            });
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t);
    }

    private volatile int sum = 0;

    @Test
    public void test2() {

        for (int i = 1; i <= 5; i++) {
            final int count = i;
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        sum = sum + count;
                    }
                }
            });
            thread1.start();
        }

        System.out.println(sum);
        System.out.println("===========================================");

    }

    private  volatile Integer sum1 = 0;

    @Test
    public void test3() {
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 5; i++) {
            final int count = i;
            service.submit(new Runnable() {
                @Override
                public void run() {
                    sum1 += count;

                }
            });
        }

        try {
            service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();

        System.out.println(sum1);

    }
}
