class Solution {
    public int[] solution(int N, int[] A) {
        int[] counters = new int[N];
        int maxCounters = 0;
        int highest = 0;
        for (int i = 0; i < A.length; i++) {
            if (A[i] <= N) {
                if (counters[A[i] - 1] < maxCounters) {
                    counters[A[i] - 1] = 1 + maxCounters;
                } else {
                    counters[A[i] - 1] += 1;
                }
                int newC = counters[A[i] - 1];
                if (newC > highest) highest = newC;
            } else {
                maxCounters = highest;
            }
        }

        for (int i = 0; i < N; i++) {
            if (counters[i] < maxCounters)
                counters[i] = maxCounters;
        }

        return counters;
    }
}
