# Integrantes: Javier Fraile Iglesias

#!/bin/bash

# inicializar variables
Ninicio=1024
Npaso=1024
Nfinal=16384
fDAT=time_slow_fast.dat
fPNG=time_slow_fast.png

# borrar el fichero DAT y el fichero PNG
rm -f $fDAT $fPNG

# generar el fichero DAT vacío
touch $fDAT

echo "Running slow and fast..."
# bucle para N desde P hasta Q
#for N in $(seq $Ninicio $Npaso $Nfinal);
for ((N = Ninicio ; N <= Nfinal ; N += Npaso)); do
	echo "N: $N / $Nfinal..."
	slowValue=0
	fastValue=0

	# ejecutar los programas slow y fast consecutivamente con tamaño de matriz N
	# para cada uno, filtrar la línea que contiene el tiempo y seleccionar la
	# tercera columna (el valor del tiempo). Dejar los valores en variables
	# para poder imprimirlos en la misma línea del fichero de datos
	for ((i = 0; i < 15; i++)); do
		slowTime1=$(./slow $N | grep 'time' | awk '{print $3}')
		slowTime2=$(./slow $N + $Npaso | grep 'time' | awk '{print $3}')
		fastTime1=$(./fast $N | grep 'time' | awk '{print $3}')
		fastTime2=$(./fast $N + $Npaso | grep 'time' | awk '{print $3}')

		slowValue=$(echo $slowTime1 + $slowValue | bc)
		fastValue=$(echo $fastTime1 + $fastValue | bc)
	done

	slowMedia=$(echo "scale=8; ($slowValue / 15)" | bc)
	fastMedia=$(echo "scale=8; ($fastValue / 15)" | bc)

	echo "$N	$slowMedia	$fastMedia" >> $fDAT
done

echo "Generating plot..."
# llamar a gnuplot para generar el gráfico y pasarle directamente por la entrada
# estándar el script que está entre "<< END_GNUPLOT" y "END_GNUPLOT"
gnuplot << END_GNUPLOT
set title "Slow-Fast Execution Time"
set ylabel "Execution time (s)"
set xlabel "Matrix Size"
set key right bottom
set grid
set term png
set output "$fPNG"
plot "$fDAT" using 1:2 with lines lw 2 title "slow", \
     "$fDAT" using 1:3 with lines lw 2 title "fast"
replot
quit
END_GNUPLOT