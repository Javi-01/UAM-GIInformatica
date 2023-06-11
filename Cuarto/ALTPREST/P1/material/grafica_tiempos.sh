#!/bin/bash

# inicializar variables
fPNG=tiempo.png
echo "Generating plot..."

gnuplot << END_GNUPLOT
set title "Tiempo"
set ylabel "Time (s)"
set xlabel "Matrix Size"
set key right bottom
set grid
set term png
set output "$fPNG"
plot "times.dat" using 1:2 with lines lw 2 title "Tiempo simple2", \
     "times.dat" using 1:3 with lines lw 2 title "Tiempo simple2_intrinsic",
replot
quit
END_GNUPLOT
