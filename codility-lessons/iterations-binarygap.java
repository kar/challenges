int i = 0;
int biggest = 0;

while (i < N) {
  int k = 2 ^ (i + 1) - 1;
  boolean found = false;
  while (k < N) {
    if (N & k == 0) {
      found = true;
      biggest = i + 1;
      break;
    }
    k = k < 1;
  }
  if (!found) {
    break;
  }
}
