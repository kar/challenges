#!/bin/python3

import sys


n = int(input().strip())
a = []
for a_i in range(n):
    a_t = [int(a_temp) for a_temp in input().strip().split(' ')]
    a.append(a_t)

acc = 0
i = 0
for row in a:
    acc += row[i] - row[n - 1 - i]
    i += 1
print(abs(acc))
