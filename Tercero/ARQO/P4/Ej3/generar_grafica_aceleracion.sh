# Integrantes: Javier Fraile Iglesias

#!/bin/bash

fPNG=aceleracion.png

echo "Generating plot..."
# llamar a gnuplot para generar el gr�fico y pasarle directamente por la entrada
# est�ndar el script que est� entre "<< END_GNUPLOT" y "END_GNUPLOT"
gnuplot << END_GNUPLOT
set title "Aceleracion"
set ylabel "Time (s)"
set yrange [0:10]
set xlabel "Matrix Size"
set key right bottom
set grid
set term png
set output "$fPNG"
plot "mult.dat" using 1:5 with lines lw 2 title "Aceleracion",
replot
quit
END_GNUPLOT