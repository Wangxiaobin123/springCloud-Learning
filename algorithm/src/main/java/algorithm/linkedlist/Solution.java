package algorithm.linkedlist;

import java.util.HashMap;
import java.util.Map;

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
        LRUCache lruCache = new LRUCache(k);
        int resultLength = 0;
        for (int[] opr : operators) {
            if (opr[0] == 2) {
                resultLength++;
            }
        }
        int[] result = new int[resultLength];
        resultLength = 0;
        int val;
        for (int[] opr : operators) {
            switch (opr[0]) {
                case 1:
                    lruCache.set(opr[1], opr[2]);
                    break;
                case 2:
                    val = lruCache.get(opr[1]);
                    result[resultLength++] = val;
                    break;
                default:
                    break;
            }
        }
        return result;
    }

}

class Node {
    public int key;
    public int val;
    public Node pre;
    public Node next;

    Node(int k, int v) {
        this.key = k;
        this.val = v;
    }
}

class DoubleList {
    public Node head, tail;
    public int size;

    public DoubleList() {
        head = new Node(0, 0);
        tail = new Node(-1, -1);
        head.next = tail;
        tail.pre = head;
        size = 0;
    }

    // 在链表尾部添加节点 x，时间 O(1)
    public void addLast(Node x) {
        x.pre = tail.pre;
        x.next = tail;
        tail.pre.next = x;
        tail.pre = x;
        size++;
    }

    // 删除链表中的 x 节点（x 一定存在）
    // 由于是双链表且给的是目标 Node 节点，时间 O(1)
    public void remove(Node x) {
        x.pre.next = x.next;
        x.next.pre = x.pre;
        size--;
    }

    // 删除链表中第一个节点，并返回该节点，时间 O(1)
    public Node removeFirst() {
        if (head.next == tail) {
            return null;
        }
        Node firstNode = head.next;
        remove(firstNode);
        return firstNode;
    }

    // 返回链表长度，时间 O(1)
    public int size() {
        return size;
    }
}
class LRUCache{
    // key -> Node(key, val)
    public Map<Integer, Node> map;
    // Node(k1, v1) <-> Node(k2, v2)...
    public DoubleList cache;
    public int capa;

    public LRUCache(int capa) {
        this.map = new HashMap<Integer, Node>();
        this.cache = new DoubleList();
        this.capa = capa;
    }


    /* 将某个 key 提升为最近使用的 */
    public void makeRecently(Integer key) {
        Node node = map.get(key);
        if (node != null) {
            cache.remove(node);
            cache.addLast(node);
        }
    }

    /* 添加最近使用的元素 */
    public void addRecently(int key, int val) {
        Node node = new Node(key, val);
        cache.addLast(node);
        map.put(key, node);
    }

    /* 删除某一个 key */
    public void deleteKey(Integer key) {
        Node node = map.get(key);
        if (node != null) {
            cache.remove(node);
            map.remove(key);
        }
    }

    /* 删除最久未使用的元素 */
    public void removeLeastRecently() {
        // 离链表尾部最近的节点是最近使用的，离链表头部最近的是最久未使用的
        Node node = cache.removeFirst();
        if (node != null) {
            map.remove(node.key);
        }
    }

    public int get(Integer key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        makeRecently(key);
        return map.get(key).val;
    }

    public void set(Integer key, Integer val) {
        // 缓存存在
        if (map.containsKey(key)) {
            deleteKey(key);
            addRecently(key, val);
            return;
        }
        if (capa == cache.size()) {
            removeLeastRecently();
        }
        // 缓存不存在
        addRecently(key, val);
    }
}