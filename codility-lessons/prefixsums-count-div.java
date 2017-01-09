class Solution {
    public int solution(int A, int B, int K) {
        // Forgot corner cases first:
        // - 0 is divisable by everything
        // - K == 1 and A == 0 (formula breaks)
        int res = (A == 0) ? 1 : 0;
        if (K > B) return res;
        res += B / K - (A - 1) / K;
        if (K == 1 && A == 0) res -= 1;
        return res;
    }
}
