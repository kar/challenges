#!/bin/python3

import sys


n = int(input().strip())

prin = "#"
for i in range(n):
    print("{:>{align}}".format(prin, align=(n)))
    prin += "#"
