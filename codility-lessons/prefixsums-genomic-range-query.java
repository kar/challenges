// This is O(n*m) (use prefix sums instead? - no)
public int[] solution(String S, int[] P, int[] Q) {
    int[] answers = new int[P.length];
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < S.length(); j++) {
            char nucleotide = S.charAt(j);
            if (nucleotide == nucleotides[i]) {
                for (int k = 0; k < P.length; k++) {
                    if (P[k] <= j && Q[k] >= j) {
                        int current = answers[k];
                        if (current == 0 || current > i + 1) {
                            current = i + 1;
                            answers[k] = current;
                        }
                    }
                }
            }
        }
    }
    return answers;
}

// This is proper
// The trick was to make a matrix that shows next position of given letter
// A, C, G, T from current position.
// Could be seen as prefix sum for each letter.

class Solution {
    public int[] solution(String S, int[] P, int[] Q) {
        int[][] next = new int[4][S.length()];
        for (int j = S.length() - 1; j >= 0; j--) {
            int current = getCurrent(S.charAt(j));
            for (int i = 0; i < 4; i++) {
                if (i == current) {
                    next[i][j] = j;
                } else if (j == (S.length() - 1)) {
                    next[i][j] = -1;
                } else {
                    next[i][j] = next[i][j+1];
                }
            }
        }

        int[] answers = new int[P.length];
        for(int j = 0; j < P.length; j++) {
            for (int i = 0; i < 4; i++) {
                int n = next[i][P[j]];
                if (n != -1 && n <= Q[j]) {
                    answers[j] = i + 1;
                    break;
                }
            }
        }
        return answers;
    }

    private int getCurrent(char c) {
        final char[] map = new char[] {'A', 'C', 'G', 'T'};
        for (int i = 0; i < 3; i++) {
            if (map[i] == c) return i;
        }
        return 3;
    }
}
