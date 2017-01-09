class Solution {
    public int solution(int[] A) {
        if (A.length == 2) return 0;
        // The slice with minimum average has to be 2 or 3 length.
        // This can be proven and I didn't come up with it :(.
        double[] twoSlices = new double[A.length - 1];
        double[] threeSlices = new double[A.length - 2];
        double min = Double.MAX_VALUE;
        double tmp;
        int minIndex = -1;

        for (int i = 1; i < A.length; i++) {
            tmp = (A[i - 1] + A[i]) / 2.0;
            twoSlices[i - 1] = tmp;
            if (tmp < min) {
                min = tmp;
                minIndex = i - 1;
            }
        }

        for (int i = 2; i < A.length; i++) {
            tmp = (A[i - 2] + A[i - 1] + A[i]) / 3.0;
            threeSlices[i - 2] = tmp;
            if (tmp < min) {
                min = tmp;
                minIndex = i - 2;
            }
        }

        return minIndex;
    }
}
