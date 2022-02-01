# Integrantes: Javier Fraile Iglesias e Iv�n Fern�ndez Par�s

#!/bin/bash

fPNG=mult_time.png

# REVISAR
echo "Generating plot..."
# llamar a gnuplot para generar el gr�fico y pasarle directamente por la entrada
# est�ndar el script que est� entre "<< END_GNUPLOT" y "END_GNUPLOT"
gnuplot << END_GNUPLOT
set title "mult-tras time"
set ylabel "Time (s)"
set xlabel "Matrix Size"
set key right bottom
set grid
set term png
set output "$fPNG"
plot "mult.dat" using 1:2 with lines lw 2 title "Time multiplicacion", \
     "mult.dat" using 1:5 with lines lw 2 title "Time traspuesta",
replot
quit
END_GNUPLOT