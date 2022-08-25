package algorithm.linkedlist;

/**
 * 反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。
 * 说明:
 * 1 ≤ m ≤ n ≤ 链表长度。
 * 示例:
 * 输入: 1->2->3->4->5->NULL, m = 2, n = 4
 * 输出: 1->4->3->2->5->NULL
 *
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
        node = deleteListNode(node, new ListNode(4));
        printLinkeNode(node);

        // 链表反转
        node = reverseListNode(node);
        printLinkeNode(node);
        node = reverseListNodeByMN(node, 2, 3);
        printLinkeNode(node);


    }

    private static ListNode reverseListNodeByMN(ListNode node, int m, int n) {
        System.out.println("反转m,n 链表，m ="+m+", n="+n);
        // write code here
        if(node == null) {
            return null;
        }
        ListNode res = new ListNode(-1);
        res.next = node; // -1->1->2->3->4->5
        ListNode pre = res; //备份指针
        //移动指针，找到m之前的位置
        for(int i = 1; i<m; i++){
            pre = pre.next; //指向1
        }
        //创建当前指针
        ListNode cur = pre.next; //指向2
        //反转链表，从m这个位置开始到n
        for(int i = m; i < n; i++){
            ListNode temp = cur.next;
            cur.next = temp.next;
            temp.next = pre.next;
            pre.next = temp;
        }
        return res.next;
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
                System.out.println("删除头结点");
                return t.next;
            }
            // 尾部节点判断
            if (t.next.value == deleteListNode.value && t.next.next == null) {
                System.out.println("删除尾部结点");
                t.next = null;
                return node;
            }
            if (t.next.value == deleteListNode.value) {
                System.out.println("删除中间结点");
                t.next = t.next.next;
                break;
            }
            t = t.next;
        }
        return node;
    }

    private static ListNode reverseListNode1(ListNode node) {
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

    private static ListNode reverseListNode(ListNode node) {
        ListNode resultList = new ListNode(-1);
        ListNode p = node;
        while (p != null) {
            ListNode next = p.next;
            ListNode t = resultList.next;
            resultList.next = new ListNode(p.value);
            resultList.next.next = t;
            p = next;
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

    /**
     *
     * @param head ListNode类
     * @return ListNode类
     */
    public ListNode deleteDuplicates (ListNode head) {
        // write code here
        // 1 1 1 2 3 -> 2 3
        // 2 3
        ListNode t = head;
        while (t!=null){

            while (t.next!=null&&t.value==t.next.value){
                t.next = t.next.next;
            }
            t = t.next;
        }
        return  head;
    }
}

class ListNode {

    int value;
    ListNode next = null;

    public ListNode(int value) {
        this.value = value;
    }


}
