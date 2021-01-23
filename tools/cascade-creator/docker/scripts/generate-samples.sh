#!/bin/bash
# nerf-rect.jpg
opencv_createsamples -img pos/$1 \
   -bg bg.txt \
   -info info/info.lst \
   -pngoutput info \
   -maxxangle 0.5 \
   -maxyangle 0.5 \
   -maxzangle 0.5 \
   -num 1300 && \
opencv_createsamples -info info/info.lst \
   -num 1300 \
   -w 80 \
   -h 16 \
   -vec positives.vec
   