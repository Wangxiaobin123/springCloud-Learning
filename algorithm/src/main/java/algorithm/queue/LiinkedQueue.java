package algorithm.queue;

/**
 * @author: wangshengbin
 * @date: 2020/12/15 下午10:24
 */
public class LiinkedQueue {
    public static void main(String[] args) {
        CircleLinkedQueue circleLinkedQueue = new CircleLinkedQueue(6);
        circleLinkedQueue.printAll();

        circleLinkedQueue.enQueue("a");
        circleLinkedQueue.enQueue("b");
        circleLinkedQueue.enQueue("c");
        circleLinkedQueue.enQueue("d");
        circleLinkedQueue.printAll();

        System.out.println("出队列：" + circleLinkedQueue.deQueue());
        circleLinkedQueue.enQueue("c");
        System.out.println("出队列：" + circleLinkedQueue.deQueue());
        circleLinkedQueue.enQueue("d");
        circleLinkedQueue.enQueue("e");
        circleLinkedQueue.printAll();
    }
}

class CircleLinkedQueue {
    private String[] items;
    private int n;
    private int head = 0;
    private int tail = 0;

    public CircleLinkedQueue(int capacity) {
        items = new String[capacity];
        n = capacity;
    }

    public boolean enQueue(String item) {
        // 判断队列已经满的条件
        if ((tail + 1) % n == head) {
            return false;
        }
        items[tail] = item;
        tail = (tail % n + 1) % n;
        return true;
    }

    public String deQueue() {
        // 队列为空的条件
        if (tail == head) {
            return null;
        }
        String deStr = items[head];
        head = (head + 1) % n;
        return deStr;
    }

    /**
     * 按照出队的顺序打印
     */
    public void printAll() {
        System.out.println("head=" + head);
        System.out.println("tail=" + tail);
        if (tail != head) {
            int headTemp = head;
            // 队列满
            while (tail != (headTemp + 1) % n) {
                System.out.println(items[headTemp]);
                headTemp = (headTemp + 1) % n;
            }
            System.out.println(items[headTemp]);
        }

        System.out.println("===================================");
    }
}
