#!/bin/sh
# pass filename of WAV file as 1st param

# vectorize WAV
HCopy -T 1 -C bin/config.txt $1 $1.bin
# convert to text file
HList -i 13 $1.bin > $1.bin.txt
# convert to CSV
cat $1.bin.txt | sed "1d;$ d" | awk -v OFS=', ' '{$1=""; $15="?"; print $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15}' > $1.csv
# cleanup temp files
rm $1.bin $1.bin.txt