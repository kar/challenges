int n = A.length;
if (K == 0 || n == 0 || K != 1 && n % K == 0) {
  return A;
}

int c = 0;
int acc = -1;
int acc2 = -1;

int i = 0;
acc = A[i];
while (c < n) {
  int newI = (i + K) % n;
  acc2 = A[newI];
  A[newI] = acc;
  acc = acc2;
  i = newI;
  c++;
}
return A;
