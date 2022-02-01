# Integrantes: Javier Fraile Iglesias e Iv�n Fern�ndez Par�s

#!/bin/bash

fPNG1=cache_escritura.png

# REVISAR
echo "Generating plot..."
# llamar a gnuplot para generar el gr�fico y pasarle directamente por la entrada
# est�ndar el script que est� entre "<< END_GNUPLOT" y "END_GNUPLOT"
gnuplot << END_GNUPLOT
set title "Slow-Fast Cache Write Failures"
set ylabel "Failueres (n�)"
set xlabel "Matrix Size"
set key right bottom
set grid
set term png
set output "$fPNG1"
plot "cache_1024.dat" using 1:3 with lines lw 2 title "write slow 1024", \
     "cache_1024.dat" using 1:5 with lines lw 2 title "write fast 1024", \
	 "cache_2048.dat" using 1:3 with lines lw 2 title "write slow 2048", \
     "cache_2048.dat" using 1:5 with lines lw 2 title "write fast 2048", \
	 "cache_4096.dat" using 1:3 with lines lw 2 title "write slow 4096", \
     "cache_4096.dat" using 1:5 with lines lw 2 title "write fast 4096", \
	 "cache_8192.dat" using 1:3 with lines lw 2 title "write slow 8192", \
     "cache_8192.dat" using 1:5 with lines lw 2 title "write fast 8192",
replot
quit
END_GNUPLOT