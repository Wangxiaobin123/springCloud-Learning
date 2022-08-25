package algorithm.synchro;

/**
 * @author: wangshengbin
 * @date: 2020/12/26 下午11:08
 */
public class DieLockTest {
    public static void main(String[] args) {
        final Object a = new Object();
        final Object b = new Object();
        Thread a1 = new Thread(new Runnable() {
            public void run() {
                synchronized (a) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (b) {

                    }
                }
            }
        }, "A");
        Thread b1 = new Thread(new Runnable() {
            public void run() {
                synchronized (b) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (a) {
                    }
                }
            }
        }, "B");

        a1.start();
        b1.start();

    }

}
