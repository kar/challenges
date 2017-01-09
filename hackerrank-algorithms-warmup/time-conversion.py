#!/bin/python3

import sys


time = input().strip().split(":")
pm = time[2].endswith("PM")
time[0] = int(time[0])
if pm:
    if time[0] < 12:
        time[0] += 12
    time[2] = time[2].strip("PM")
else:
    if time[0] == 12:
        time[0] = 0
    time[2] = time[2].strip("AM")

print("{:02d}:{}:{}".format(time[0], time[1], time[2]))
