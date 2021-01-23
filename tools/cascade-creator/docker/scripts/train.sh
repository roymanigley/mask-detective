#!/bin/bash
opencv_traincascade \
   -data data \
   -vec positives.vec \
   -bg bg.txt \
   -numPos 1200 \
   -numNeg 600 \
   -numStages 10 \
   -w 80 \
   -h 16