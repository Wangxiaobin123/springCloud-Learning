package algorithm.queue;

/**
 * @author: wangshengbin
 * @date: 2020/12/15 下午10:24
 */
public class LiinkedQueue {
    public static void main(String[] args) {
        CircleLinkedQueue circleLinkedQueue = new CircleLinkedQueue(3);
        circleLinkedQueue.printAll();
        circleLinkedQueue.enQueue("a");
        circleLinkedQueue.enQueue("b");
        circleLinkedQueue.enQueue("c");
        circleLinkedQueue.enQueue("d");
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

    public void deQueue() {

    }

    public void printAll() {
        System.out.println("head=" + head);
        System.out.println("tail=" + tail);
        for (int i = head; i < tail; i++) {
            System.out.println(items[i]);
        }
        System.out.println("===================================");
    }
}
