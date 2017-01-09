#!/bin/python3

import sys


a0,a1,a2 = input().strip().split(' ')
a0,a1,a2 = [int(a0),int(a1),int(a2)]
b0,b1,b2 = input().strip().split(' ')
b0,b1,b2 = [int(b0),int(b1),int(b2)]

a = [a0, a1, a2]
b = [b0, b1, b2]

aa, bb = 0, 0
for i, x in enumerate(a):
    if b[i] < x:
        aa += 1
    elif b[i] > x:
        bb += 1

print('{0} {1}'.format(aa, bb))
