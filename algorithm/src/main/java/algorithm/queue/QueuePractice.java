package algorithm.queue;

/**
 * @author: wangshengbin
 * @date: 2020/12/14 下午11:16
 */
public class QueuePractice {
    public static void main(String[] args) {
        ArrayQueue arrayQueue = new ArrayQueue(10);
        arrayQueue.enQueue(1);
        arrayQueue.enQueue(2);
        arrayQueue.enQueue(3);
        arrayQueue.enQueue(4);
        arrayQueue.enQueue(5);
        arrayQueue.enQueue(6);
        arrayQueue.enQueue(7);
        arrayQueue.enQueue(8);
        arrayQueue.enQueue(9);
        arrayQueue.enQueue(10);
        arrayQueue.printAll();
        arrayQueue.dequeue();
        arrayQueue.dynamicEnQueue(11);
        arrayQueue.printAll();
    }
}

class ArrayQueue {
    public int[] items;
    public int n = 0;
    public int head = 0;
    public int tail = 0;

    /**
     * 申请 capacity 的队列
     *
     * @param capacity
     */
    public ArrayQueue(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Error capacity,capacity = " + capacity);
        }
        items = new int[capacity];
        n = capacity;
    }


    /**
     * 入队操作
     *
     * @param number
     * @return
     */
    public boolean enQueue(int number) {
        if (tail == n) {
            return false;
        }
        items[tail] = number;
        tail++;
        return true;
    }

    public boolean dynamicEnQueue(int number) {
        if (tail == n) {
            if (head == 0) {
                return false;
            }
            for (int i = head; i < tail; i++) {
                items[i - head] = items[i];
            }
            tail = tail - head;
            head = 0;
        }
        items[tail] = number;
        tail++;
        return true;
    }


    /**
     * 出队操作
     *
     * @return
     */
    public Integer dequeue() {
        if (head == tail) {
            return null;
        }
        Integer number = items[head];
        head++;
        return number;
    }

    public void printAll() {
        System.out.println("head=" + head);
        System.out.println("tail=" + tail);
        for (int i = head; i < tail; i++) {
            System.out.println(items[i]);
        }
    }


}
