package algorithm.linkedlist;

/**
 * @author: wangshengbin
 * @date: 2021/1/13 上午11:16
 */
public class LruTest {

    /**
     * 第一次操作后：最常使用的记录为("1", 1)
     * 第二次操作后：最常使用的记录为("2", 2)，("1", 1)变为最不常用的
     * 第三次操作后：最常使用的记录为("3", 2)，("1", 1)还是最不常用的
     * 第四次操作后：最常用的记录为("1", 1)，("2", 2)变为最不常用的
     * 第五次操作后：大小超过了3，所以移除此时最不常使用的记录("2", 2)，加入记录("4", 4)，并且为最常使用的记录，然后("3", 2)变为最不常使用的记录
     *
     * @param args
     */
    public static void main(String[] args) {
        // [[1,1,1],[1,2,2],[1,3,2],[2,1],[1,4,4],[2,2]],3
        int[][] value = new int[6][];
        value[0] = initArray(1, 1, 1);
        value[1] = initArray(1, 2, 2);
        value[2] = initArray(1, 3, 2);
        value[3] = initArray(2, 1);
        value[4] = initArray(1, 4, 4);
        value[5] = initArray(2, 2);
        int[] result = LRU(value, 3);
        printArray(result);
    }

    public static int[] initArray(int i, int i1, int i2) {
        int[] t = new int[3];
        t[0] = i;
        t[1] = i1;
        t[2] = i2;
        return t;
    }

    public static int[] initArray(int i1, int i2) {
        int[] t = new int[2];
        t[0] = i1;
        t[1] = i2;
        return t;
    }

    public static void printArray(int[] result) {
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
    }

    /**
     * lru design
     *
     * @param operators int整型二维数组 the ops
     * @param k         int整型 the k
     * @return int整型一维数组
     */
    public static int[] LRU(int[][] operators, int k) {
        return null;
    }
}



