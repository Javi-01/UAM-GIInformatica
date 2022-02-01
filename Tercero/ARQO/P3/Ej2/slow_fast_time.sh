# Integrantes: Javier Fraile Iglesias e Iván Fernández París

#!/bin/bash

# inicializar variables
#Como somos la pareja 10, el valor de P será 3 ya que es el valor de “10 mod 7”.
Ninicio=(1024 + 1024*3)
Npaso=256
Nfinal=(1024 + 1024*4)

TAMcacheinicio=1024
TAMcachefinal=8192
Ltam=8388608

Nvias=1
TAMlinea=64

fPNG1=cache_lectura.png
fPNG2=cache_escritura.png

# borrar el fichero DAT y el fichero PNG
rm -f $fDAT $fPNG1 $fPNG2

# generar el fichero DAT vacío
touch $fDAT

echo "Running slow and fast..."
# bucle para N desde P hasta Q 
#for N in $(seq $Ninicio $Npaso $Nfinal);

# Preguntar acerca de si 1 por configuracion se refiere a el TAM o al bucle for de 0 hasta 15
for ((TAM = TAMcacheinicio ; N <= TAMcachefinal ; TAM = (TAM*2))); do # Para solo hacer un ciclo por tamaño de de cache 
	
	for ((N = Ninicio ; N <= Nfinal ; N += Npaso)); do
		echo "TAM cache: $TAM"
		echo "N: $N / $Nfinal..."
		D1mr_slow=0
		D1mr_fast=0
		D1mw_slow=0
		D1mw_fast=0

		
		# Indicamos las opciones de I1, D1 y LL con su size, asociatividad (siempre 1) y tamaño de linea (64)
		valgrind --tool=cachegrind --I1=$TAM,$Nvias,$TAMlinea  --D1=$TAM,$Nvias,$TAMlinea --LL=$Ltam,$Nvias,$TAMlinea --cachegrind-out-file=valgrind_slow.dat ./slow $N
		cg_annotate valgrind_slow.dat | head -n 30

		# Recogemos los valores de la ultima del fichero valgrind_slow.dat
		# que es donde salen los valores de lecturas producidas, ...
		slowRead=$(tail -1 valgrind_slow.dat | awk '{print $6}') # Cogemos D1mr
		slowWrite=$(tail -1 valgrind_slow.dat | awk '{print $9}') # Cogemos D1mw
		# ¿EJECUTAR DE NUEVO VALGRIND?

		# Indicamos las opciones de I1, D1 y LL con su size, asociatividad (siempre 1) y tamaño de linea (64)
		valgrind --tool=cachegrind --I1=$TAM,$Nvias,$TAMlinea  --D1=$TAM,$Nvias,$TAMlinea --LL=$Ltam,$Nvias,$TAMlinea --cachegrind-out-file=valgrind_fast.dat ./fast $N
		cg_annotate valgrind_fast.dat | head -n 30

		# Recogemos los valores de la ultima del fichero valgrind_fast.dat
		# que es donde salen los valores de lecturas producidas, ...
		fastRead=$(tail -1 valgrind_fast.dat | awk '{print $6}')
		fastWrite=$(tail -1 valgrind_fast.dat | awk '{print $9}')
		# ¿EJECUTAR DE NUEVO VALGRIND?

		D1mr_slow=$(echo "scale=8; ($slowRead + $D1mr_slow)" | bc)
		D1mw_slow=$(echo "scale=8; ($slowWrite + $D1mw_slow)" | bc)
		D1mr_fast=$(echo "scale=8; ($fastRead + $D1mr_fast)" | bc)
		D1mw_fast=$(echo "scale=8; ($fastWrite + $D1mw_fast)" | bc)

		echo "$N	$D1mr_slow $D1mw_slow $D1mr_fast $D1mw_fast" >> cache_$TAM.dat
	done
done

# REVISAR
echo "Generating plot..."
# llamar a gnuplot para generar el gráfico y pasarle directamente por la entrada
# estándar el script que está entre "<< END_GNUPLOT" y "END_GNUPLOT"
gnuplot << END_GNUPLOT
set title "Slow-Fast Cache Read Failures"
set ylabel "Failueres (nº)"
set xlabel "Matrix Size"
set key right bottom
set grid
set term png
set output "$fPNG1"
plot "cache_1024.dat" using 1:2 with lines lw 2 title "read slow 1024", \
     "cache_1024.dat" using 1:4 with lines lw 2 title "read fast 1024", \
	 "cache_2048.dat" using 1:2 with lines lw 2 title "read slow 2048", \
     "cache_2048.dat" using 1:4 with lines lw 2 title "read fast 2048", \
	 "cache_4096.dat" using 1:2 with lines lw 2 title "read slow 4096", \
     "cache_4096.dat" using 1:4 with lines lw 2 title "read fast 4096", \
	 "cache_8192.dat" using 1:2 with lines lw 2 title "read slow 8192", \
     "cache_8192.dat" using 1:4 with lines lw 2 title "read fast 8192",
replot
quit

set title "Slow-Fast Cache Write Failures"
set ylabel "Failueres (nº)"
set xlabel "Matrix Size"
set key right bottom
set grid
set term png
set output "$fPNG2"
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
