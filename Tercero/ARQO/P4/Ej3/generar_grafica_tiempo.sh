# Integrantes: Javier Fraile Iglesias

#!/bin/bash

fPNG=tiempo.png

echo "Generating plot..."
# llamar a gnuplot para generar el gr�fico y pasarle directamente por la entrada
# est�ndar el script que est� entre "<< END_GNUPLOT" y "END_GNUPLOT"
gnuplot << END_GNUPLOT
set title "Tiempo"
set ylabel "Time (s)"
set xlabel "Matrix Size"
set key right bottom
set grid
set term png
set output "$fPNG"
plot "mult.dat" using 1:3 with lines lw 2 title "Tiempo serie", \
     "mult.dat" using 1:4 with lines lw 2 title "Tiempo bucle3",
replot
quit
END_GNUPLOT