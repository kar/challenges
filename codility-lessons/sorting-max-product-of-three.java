// This solution is correct, but performance isn't optimal.
// Codility shows O(n^3), but it doesn't quite make sense.
class Solution {
  public int solution(int[] A) {
    // First, balance the heap
    int size = A.length;
    heap(A, size);

    // Then, keep moving the highest (first) number to the end
    // and shorten the heap, rebalance
    while (size > 0) {
      swap(A, 0, size - 1)
      size--;
      heap(A, size);
    }

    // Finally, just multiply 3 last items since they are the biggest
    int s = A.length;
    int res = A[s - 1] * A[s - 2] * A[s - 3];

    // Two negative numbers will make a positive number.
    // It may be better than the above solution.
    int alt = A[0] * A[1] * A[s - 1];
    return (alt > res) ? alt : res;
  }

  private void heap(int[] A, int size) {
    for(int i = size - 1; i >= 0; i--) {
      int parent = (i - 1) / 2;
      if (parent < 0) break;
      if (A[parent] < A[i]) {
        swap(A, parent, i);
      }
    }
  }

  private void swap(int[] A, int a, int b) {
    int tmp = A[a];
    A[a] = A[b];
    A[b] = tmp;
  }
}

// Second attempt with optimal performance.
class Solution {
  public int solution(int[] A) {
    // First, balance the heap
    int size = A.length;
    heap(A, size);

    // Then, keep moving the highest (first) number to the end
    // and shorten the heap, rebalance
    while (size > 0) {
      swap(A, 0, size - 1)
      size--;
      heap(A, size);
    }

    // Finally, just multiply 3 last items since they are the biggest
    int s = A.length;
    int res = A[s - 1] * A[s - 2] * A[s - 3];

    // Two negative numbers will make a positive number.
    // It may be better than the above solution.
    int alt = A[0] * A[1] * A[s - 1];
    return (alt > res) ? alt : res;
  }

  private void heap(int[] A, int size) {
    for(int i = size - 1; i >= 0; i--) {
      int parent = (i - 1) / 2;
      if (parent < 0) break;
      if (A[parent] < A[i]) {
        swap(A, parent, i);
      }
    }
  }

  private void swap(int[] A, int a, int b) {
    int tmp = A[a];
    A[a] = A[b];
    A[b] = tmp;
  }
}
