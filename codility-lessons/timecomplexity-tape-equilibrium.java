import java.lang.Math;

class Solution {
    public int solution(int[] A) {
        int n = A.length;
        int sumA = A[0];
        int sumB = 0;
        for (int i = 1; i < n; i++) {
          sumB += A[i];
        }

        int min = Math.abs(sumA - sumB);
        int minIndex = 1;

        for (int i = 1; i < n - 1; i++) {
          sumA += A[i];
          sumB -= A[i];

          if (Math.abs(sumA - sumB) < min) {
            min = Math.abs(sumA - sumB);
            minIndex = i + 1;
          }
        }
        return min;
    }
}
