package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CountDownLatch count 测试
 *
 * @author: wangshengbin
 * @date: 2021/12/14 下午5:04
 */
public class CountDownLatchMain {

    private static final int CPU_CORES;
    private static final int LOOP = 256;

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        List<String> l = new ArrayList<>();
        for (int i = 0; i < LOOP; i++) {
            l.add(i + "");
        }
        CountDownLatch latch = new CountDownLatch(l.size());
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(CPU_CORES, CPU_CORES, 0L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(256));
        for (String str : l) {
            poolExecutor.submit(() -> {
                System.out.println(str);
                atomicInteger.incrementAndGet();
                latch.countDown();
            });
        }
        boolean await = latch.await(10, TimeUnit.SECONDS);
        while (await) {
            long latchCount = latch.getCount();
            long completedTaskCount = poolExecutor.getCompletedTaskCount();
            int size = poolExecutor.getQueue().size();
            if (latchCount == 0) {
                await = false;
            }
            System.out.println("latchCount " + latchCount + ",completedTaskCount " + completedTaskCount + ",queueSize " + size);
        }
        poolExecutor.shutdown();
        System.out.println("num " + atomicInteger.get());
    }

    static {
        CPU_CORES = Runtime.getRuntime().availableProcessors();
        System.err.println("CPU_CORES " + CPU_CORES);
    }
}
