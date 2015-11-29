#!/bin/sh

# pass dir name of bin files as parameter
for file in $1*.bin.txt
do
    letter=`echo $file | awk -F. '{print $1}'| awk -F/ '{print $2}'`
    cat $file | sed "1d;$ d" | sort -r  | head -n 2000 | awk -v OFS=', ' '{$1=""; $15="'$letter'"; print $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15}'
done
