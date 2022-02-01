# Integrantes: Javier Fraile Iglesias e Iv�n Fern�ndez Par�s

#!/bin/bash

fPNG=mult_cache.png

# REVISAR
echo "Generating plot..."
# llamar a gnuplot para generar el gr�fico y pasarle directamente por la entrada
# est�ndar el script que est� entre "<< END_GNUPLOT" y "END_GNUPLOT"
gnuplot << END_GNUPLOT
set title "Cache Failures"
set ylabel "Failueres (n�)"
set xlabel "Matrix Size"
set key right bottom
set grid
set term png
set output "$fPNG"
plot "mult.dat" using 1:3 with lines lw 2 title "Read multiplicacion", \
     "mult.dat" using 1:4 with lines lw 2 title "Write multiplicaion", \
     "mult.dat" using 1:6 with lines lw 2 title "Read traspuesta", \
     "mult.dat" using 1:7 with lines lw 2 title "Write traspuesta",
replot
quit
END_GNUPLOT