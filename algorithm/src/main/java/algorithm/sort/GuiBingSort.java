package algorithm.sort;


/**
 * @author: wangshengbin
 * @date: 2020/12/20 下午10:20
 */
public class GuiBingSort {

    public static void main(String[] args) {
        int[] numbers = new int[5];
        numbers[0] = 5;
        numbers[1] = 2;
        numbers[2] = 3;
        numbers[3] = 1;
        numbers[4] = 4;
        printArray(numbers);
        mergeSort(numbers, 0, 4);
        printArray(numbers);
    }

    private static void mergeSort(int[] numbers, int p, int r) {
        if (p >= r) {
            return;
        }
        int q = (p + r) / 2;
        mergeSort(numbers, p, q);
        mergeSort(numbers, q + 1, r);
        // 合并分割后的两个数组
        merge(numbers, p, q, r);
    }

    private static void merge(int[] numbers, int p, int q, int r) {
        int i = p;
        int j = q + 1;
        int k = 0;
        int[] temp = new int[r - p + 1];
        while (i <= q && j <= r) {
            if (numbers[i] <= numbers[j]) {
                temp[k] = numbers[i];
                k++;
                i++;
            } else {
                temp[k] = numbers[j];
                k++;
                j++;
            }
        }

        int start = i;
        int end = q;
        if (j <= r) {
            start = j;
            end = r;
        }
        while (start <= end) {
            temp[k] = numbers[start];
            k++;
            start++;
        }

        // 将tmp中的数组拷贝回a[p...r]
        for (i = 0; i <= (r - p); i++) {
            numbers[p + i] = temp[i];
        }


    }

    private static void printArray(int[] numbers) {
        for (int n : numbers) {
            System.out.print(n + ",");
        }
        System.out.println();
        System.out.println("====================================");
    }

}
