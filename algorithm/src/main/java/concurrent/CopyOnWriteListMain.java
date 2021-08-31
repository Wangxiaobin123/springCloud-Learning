package concurrent;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: wangshengbin
 * @date: 2021/2/8 下午6:02
 */
public class CopyOnWriteListMain {
    public static void main(String[] args) {
        final CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();

        final Random random = new Random();

        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String s = random.nextInt(50) + "  ";
                        System.out.println("写随机数：" + s);
                        copyOnWriteArrayList.add(s);
                    }
                });
            } else {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        StringBuffer sb = new StringBuffer();
                        for (String s : copyOnWriteArrayList) {
                            sb.append(s);
                        }
                        System.out.println("read:" + sb.toString());
                    }
                });
            }
        }
        executorService.shutdown();
    }
}
