package algorithm;

import java.util.concurrent.*;

/**
 * @author: wangshengbin
 * @date: 2021/2/4 下午8:36
 */
public class TestAli {
    public static void main(final String[] args) {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(20, 20,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        final CountDownLatch countDownLatch = new CountDownLatch(3);

        final String[] arr = new String[3];
        for (int i = 0; i < 26; i++) {
            final int j = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {

                    arr[j] = "true";
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        System.out.println(arr.length);
        System.out.println("==================");

        long taskCount = executorService.getTaskCount();
        int queueSize = executorService.getQueue().size();
        int activeCount = executorService.getActiveCount();
        long completedTaskCount = executorService.getCompletedTaskCount();

        System.out.println("taskCount = " + taskCount + ",\n"
                + "queueSize = " + queueSize + "\n"
                + "activeCount = " + activeCount + "\n"
                + "completedTaskCount = " + completedTaskCount);
    }
}
