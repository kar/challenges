#!/bin/python3

import sys


n = int(input().strip())
arr = [int(arr_temp) for arr_temp in input().strip().split(' ')]
acc = 0
for x in arr:
    acc += x
print(acc)
