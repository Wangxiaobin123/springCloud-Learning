package algorithm.synchro;

/**
 * @author: wangshengbin
 * @date: 2021/2/19 下午5:09
 */
public class InitThread1 extends Thread {
    @Override
    public void run() {
        System.out.println("继承Thread 并且重写 Run 方法");
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + "-" + i);
        }
        System.out.println("run 方法结束");
    }

    public static void main(String[] args) {

        System.out.println("调用main方法");
        for(int i = 0;i<10;i++){
            if(i == 1){
                InitThread1 initThread1 = new InitThread1();
                initThread1.start();
            }
        }

        System.out.println("结束main方法");

    }
}
