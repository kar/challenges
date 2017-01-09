#!/bin/python3

import sys


n = int(input().strip())
arr = [int(arr_temp) for arr_temp in input().strip().split(' ')]

pos = 0
neg = 0

for item in arr:
    if item > 0:
        pos +=1
    elif item < 0:
        neg += 1

print("{0:.6f}".format(pos / n))
print("{0:.6f}".format(neg / n))
print("{0:.6f}".format((n - pos - neg) / n))
