class Solution {
    public int solution(int[] A) {
        int multiplier = 0;
        int sum = 0;
        for (int i = 0; i < A.length; i++) {
            if (A[i] == 0) {
                multiplier += 1;
            } else {
                sum += multiplier;
                if (sum > 1000000000) {
                  sum = -1;
                  break;
                }
            }
        }
        return sum;
    }
}
