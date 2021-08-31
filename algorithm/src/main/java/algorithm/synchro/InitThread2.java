package algorithm.synchro;

/**
 * @author: wangshengbin
 * @date: 2021/2/19 下午6:06
 */
public class InitThread2 implements Runnable {
    @Override
    public void run() {
        System.out.println("通过实现Runnable创建线程");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new InitThread2());
        thread.start();
        // 线程私有 程序计数器 Java虚拟机栈 本地方方法栈

        // 线程共享 堆 方法区
    }
}
