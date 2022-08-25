package algorithm.synchro;

import java.util.concurrent.*;

/**
 * @author: wangshengbin
 * @date: 2021/2/20 上午11:06
 */
public class InitThread3 implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + "调用call方法");
        Thread.sleep(3000);
        return 1;
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "调用main方法");
        InitThread3 thread3 = new InitThread3();

        FutureTask<Integer> futureTask = new FutureTask<>(thread3);
        Thread thread = new Thread(futureTask);
        thread.start();
        try {
            Integer integer = futureTask.get(1,TimeUnit.SECONDS);
            System.out.println("value =" + integer);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println("执行完毕main方法");
    }
}
