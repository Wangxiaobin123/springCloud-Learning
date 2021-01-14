package algorithm.linkedlist;

/**
 * @author: wangshengbin
 * @date: 2021/1/7 下午5:11
 */
public class LinkNodeTest {
    public static void main(String[] args) {
        // 初始化
        ListNode node = initNode(5);
        printLinkeNode(node);
        // 尾部添加节点
        ListNode addNode = new ListNode(9);
        addNode(node, addNode);
        printLinkeNode(node);
        // 头部添加节点
        ListNode addHeadNode = new ListNode(0);
        addHeadNode(node, addHeadNode);
        node = addHeadNode;
        printLinkeNode(node);
        // 删除头部节点
        node = deleteListNode(node, addHeadNode);
        printLinkeNode(node);
        // 删除尾部节点
        node = deleteListNode(node, addNode);
        printLinkeNode(node);
        // 删除中间节点
        node = deleteListNode(node, new ListNode(8));
        printLinkeNode(node);

        // 链表反转
        node = reverseListNode(node);
        printLinkeNode(node);


    }

    /**
     * 删除节点
     *
     * @param node
     * @param deleteListNode
     * @return
     */
    private static ListNode deleteListNode(ListNode node, ListNode deleteListNode) {
        ListNode t = node;
        while (t.next != null) {
            // 头结点判断
            if (t.value == deleteListNode.value && node.value == deleteListNode.value) {
                return t.next;
            }
            // 尾部节点判断
            if (t.next.value == deleteListNode.value && t.next.next == null) {
                t.next = null;
                return node;
            }
            if (t.next.value == deleteListNode.value) {
                t.next = t.next.next;
                break;
            }
            t = t.next;
        }
        return node;
    }

    private static ListNode reverseListNode(ListNode node) {
        ListNode resultList = new ListNode(-1);
        ListNode p = node;
        while (p != null) {
            ListNode t = p.next;
            p.next = resultList.next;
            resultList.next = p;
            p = t;
        }
        return resultList.next;

    }


    /**
     * 头部添加节点
     *
     * @param node
     * @param addHeadNode
     */
    private static void addHeadNode(ListNode node, ListNode addHeadNode) {
        ListNode t = addHeadNode;
        while (t.next != null) {
            t = t.next;
        }
        t.next = node;
    }

    /**
     * 尾部添加节点
     *
     * @param node
     * @param addNode
     */
    private static void addNode(ListNode node, ListNode addNode) {
        ListNode t = node;
        while (t.next != null) {
            t = t.next;
        }
        t.next = addNode;
    }

    private static void printLinkeNode(ListNode node) {
        while (node.next != null) {
            System.out.print(node.value + "->");
            node = node.next;
        }
        System.out.print(node.value + "->" + node.next);
        System.out.println();
        System.out.println("========================");
    }

    /**
     * 初始化节点
     *
     * @param number
     * @return
     */
    private static ListNode initNode(int number) {
        ListNode head = new ListNode(1);
        ListNode next = head;
        int i = next.value;
        while (i < number) {
            i++;
            if (next.next == null) {
                next.next = new ListNode(i);
            }
            next = next.next;
        }
        return head;
    }
}

class ListNode {

    int value;
    ListNode next = null;

    public ListNode(int value) {
        this.value = value;
    }


}
