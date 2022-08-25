package algorithm.linkedlist;

/**
 * @author: wangshengbin
 * @date: 2020/12/12 下午11:13
 */
public class LinkedListPractice {
    // 链表反转
    // 1->2->3->4->5->null
    // 5->4->3->2->1->null

    public static void main(String[] args) {

        LinkedNode linkedNode = initLinkedNode();

        LinkedNode node = new LinkedNode(7);
        addLinkedNode(linkedNode, node);

        printLinkedNode(linkedNode);

        node = new LinkedNode(7);
        linkedNode = deleteNode(linkedNode, node);
        printLinkedNode(linkedNode);

    }

    private static LinkedNode deleteNode(LinkedNode linkedNode, LinkedNode node) {
        LinkedNode temp = linkedNode;
        if (temp.value == node.value) {
            temp = temp.next;
            return temp;

        }
        while (temp.next.value != node.value) {
            temp = temp.next;
        }
        if (temp.next.next != null) {
            temp.next = temp.next.next;
        } else {
            temp.next = null;
        }
        return linkedNode;
    }

    /**
     * 打印节点
     *
     * @param linkedNode
     */
    private static void printLinkedNode(LinkedNode linkedNode) {
        while (linkedNode != null) {
            System.out.println(linkedNode.value);
            linkedNode = linkedNode.next;
        }
        System.out.println("======================");
    }


    /**
     * // 添加节点
     *
     * @param linkedNode
     * @param node
     */
    private static void addLinkedNode(LinkedNode linkedNode, LinkedNode node) {
        LinkedNode temp = linkedNode;
        do {
            temp = temp.next;
        } while (temp.next != null);
        temp.next = node;
    }


    /**
     * 初始化单链表
     *
     * @return
     */
    private static LinkedNode initLinkedNode() {
        LinkedNode head = new LinkedNode(1);
        LinkedNode last = head;
        int i = 1;
        do {
            i++;
            if (last.next == null) {
                last.next = new LinkedNode(i);
            }
            last = last.next;
        } while (i < 5);

        return head;
    }


    public LinkedNode reverseList(LinkedNode head) {
        return null;
    }

}

/**
 * 定义链表
 */
class LinkedNode {
    int value;
    LinkedNode next;

    LinkedNode(int value) {
        this.value = value;
    }
}
