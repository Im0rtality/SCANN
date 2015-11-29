#!/bin/sh
mkdir output/
HCopy -T 1 -C config.txt input/$1 output/$1.bin
HList -i 13 output/$1.bin > output/$1.bin.txt