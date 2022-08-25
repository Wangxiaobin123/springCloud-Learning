package algorithm.linkedlist;

import static algorithm.linkedlist.LruTest.initArray;


/**
 * @author: wangshengbin
 * @date: 2021/1/13 下午4:52
 */
public class Solution {

    public static void main(String[] args) {
        int[][] value = new int[6][];
        value[0] = initArray(1, 1, 1);
        value[1] = initArray(1, 2, 2);
        value[2] = initArray(1, 3, 2);
        value[3] = initArray(2, 1);
        value[4] = initArray(1, 4, 4);
        value[5] = initArray(2, 2);
        int[] result = LRU(value, 3);
        System.out.println(result[0] + "----->" + result[1]);

        DoubleList doubleList = new DoubleList();
        doubleList.addLast(new Node(1, 1));
        doubleList.addLast(new Node(2, 2));
        doubleList.remove(new Node(3, 3));
        Node node = doubleList.removeFirst();
        System.out.println(node.val);

    }

    /**
     * lru design
     * <p>
     * 第一次操作后：最常使用的记录为("1", 1)
     * 第二次操作后：最常使用的记录为("2", 2)，("1", 1)变为最不常用的
     * 第三次操作后：最常使用的记录为("3", 2)，("1", 1)还是最不常用的
     * 第四次操作后：最常用的记录为("1", 1)，("2", 2)变为最不常用的
     * 第五次操作后：大小超过了3，所以移除此时最不常使用的记录("2", 2)，加入记录("4", 4)，并且为最常使用的记录，然后("3", 2)变为最不常使用的记录
     *
     * @param operators int整型二维数组 the ops
     * @param k         int整型 the k
     * @return int整型一维数组
     */
    public static int[] LRU(int[][] operators, int k) {
        int resultLength = 0;
        for (int[] opr : operators) {
            if (opr[0] == 2) {
                resultLength++;
            }
        }
        int[] result = new int[resultLength];
        resultLength = 0;
        LruCache lruCache = new LruCache(k);
        for (int[] opr : operators) {
            switch (opr[0]) {
                case 1:
                    lruCache.put(opr[1], opr[2]);
                    break;
                case 2:
                    int val = lruCache.get(opr[1]);
                    result[resultLength++] = val;
                    break;
                default:
                    break;
            }
        }

        return result;
    }
}

/**
 * 链表的结构
 */
class Node {
    public int key;
    public int val;
    public Node pre;
    public Node next;

    public Node(int k, int v) {
        this.key = k;
        this.val = v;
    }
}

class DoubleList {
    public Node head;
    public Node tail;
    public int size;
    // 初始化双向链表

    public DoubleList() {
        this.size = 0;
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.pre = head;
    }

    // 在链表尾部添加节点 x，时间 O(1)
    public void addLast(Node x) {
        if (x != null) {
            tail.pre.next = x;
            x.pre = tail.pre;
            x.next = tail;
            tail.pre = x;
            size++;
        }
    }

    // 由于是双链表且给的是目标 Node 节点，时间 O(1)
    public void remove(Node x) {
        if (x != null && x.next != null) {
            x.pre.next = x.next;
            x.next.pre = x.pre;
            size--;
        }
    }

    // 删除链表中第一个节点，并返回该节点，时间 O(1)
    public Node removeFirst() {
        if (head.next == tail) {
            return null;
        }
        Node first = head.next;
        remove(first);
        return first;
    }

    // 返回链表长度，时间 O(1)
    public int getSize() {
        return size;
    }
}

class LruCache {
    public Node[] hashNode;
    public DoubleList cache;
    public int size;

    public LruCache(int cap) {
        this.size = cap;
        cache = new DoubleList();
        hashNode = new Node[cap];
    }

    /* 将某个 key 提升为最近使用的 */
    public void makeKeyRecently(int key) {
        int index = getIndex(key);
        if (hashNode[index] != null) {
            Node node = hashNode[index];
            cache.remove(node);
            cache.addLast(node);
        }
    }

    /* 添加最近使用的元素 */
    public void addRecently(int key, int val) {
        if (size == cache.getSize()) {
            deleteLastRecently();
        }
        Node node = new Node(key, val);
        cache.addLast(node);
        int index = getIndex(key);
        hashNode[index] = node;

    }

    /* 删除某一个 key */
    public void deleteKey(int key) {
        int index = getIndex(key);
        if (hashNode[index] != null) {
            cache.remove(hashNode[index]);
            hashNode[index] = null;
        }
    }

    /* 删除最久未使用的元素 */
    public void deleteLastRecently() {
        Node node = cache.removeFirst();
        int index = getIndex(node.key);
        if (hashNode[index] != null) {
            hashNode[index] = null;
        }

    }

    public int get(int key) {
        int index = getIndex(key);
        if (hashNode[index] != null) {
            Node node = hashNode[index];
            makeKeyRecently(key);
            if (size == cache.size) {
                if (cache.head.next != null) {
                    hashNode[getIndex(cache.head.next.key)] = null;
                }
            }
            return node.key != key ? -1 : node.val;
        }
        return -1;
    }

    public void put(int key, int val) {
        int index = getIndex(key);
        if (hashNode[index] != null) {
            deleteKey(key);
            addRecently(key, val);
            return;
        }

        if (size == cache.size) {
            deleteLastRecently();
        }
        addRecently(key, val);

    }

    public int getIndex(int key) {
        return (key < 0 ? (key == Integer.MIN_VALUE ? 0 : -key) : key) % hashNode.length;
    }

}



