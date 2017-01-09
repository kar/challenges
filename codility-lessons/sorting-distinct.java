package gs.kar.challenges.codility;

import java.util.Arrays;

public class SortingDistinct {

    public static void main(String[] args) {
        int[] arr = Arrays.stream(args).map(String::trim).mapToInt(Integer::parseInt).toArray();
        int sol = solution(arr);
        System.out.println(Arrays.toString(arr));
        System.out.println(sol);
    }

    private static int solution(int[] A) {
        if (A.length == 0) return 0;
        // Merge sort
        A = sort(A);
        // Count distinct items
        Integer current = null;
        int acc = 0;
        for (int i = 0; i < A.length; i++) {
            if (current == null || A[i] != current) {
                current = A[i];
                acc++;
            }
        }
        return acc;
    }

    private static int[] sort(int[] arr) {
        if (arr.length == 1) return arr;
        int split = arr.length / 2;
        int[] a1 = new int[split];
        int[] a2 = new int[arr.length - split];
        for (int i = 0; i < split; i++) {
            a1[i] = arr[i];
        }
        for (int i = split; i < arr.length; i++) {
            a2[i - split] = arr[i];
        }
        a1 = sort(a1);
        a2 = sort(a2);
        int i = 0, j = 0, k = 0;
        while (j < a1.length && k < a2.length) {
            if (a1[j] <= a2[k]) {
                arr[i] = a1[j];
                j++;
            } else {
                arr[i] = a2[k];
                k++;
            }
            i++;
        }
        while (j < a1.length) {
            arr[i++] = a1[j++];
        }
        while (k < a2.length) {
            arr[i++] = a2[k++];
        }
        return arr;
    }
}
