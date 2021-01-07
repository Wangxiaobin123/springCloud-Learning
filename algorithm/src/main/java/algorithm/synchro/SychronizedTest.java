package algorithm.synchro;

/**
 * @author: wangshengbin
 * @date: 2020/12/23 下午10:48
 */
public class SychronizedTest {
    public void test1()
    {
        synchronized(this)
        {
            int i = 5;
            while( i-- > 0)
            {
                System.out.println(Thread.currentThread().getName() + " : " + i);
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException ie)
                {
                }
            }
        }
    }
    public synchronized void test2()
    {
        int i = 5;
        while( i-- > 0)
        {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException ie)
            {
            }
        }
    }


    public static void main(String[] args) {
        final SychronizedTest test = new SychronizedTest();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                test.test1();
            }
        }, "test1");

        Thread t2 = new Thread(new Runnable() {
            public void run() {
               test.test2();
            }
        }, "test2");

        t1.start();

        t2.start();




    }
}
