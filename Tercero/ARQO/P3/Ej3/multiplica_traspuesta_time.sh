# Integrantes: Javier Fraile Iglesias e Iván Fernández París

#!/bin/bash

# inicializar variables
Ninicio=256+256*3
Npaso=32
Nfinal=256+256*4
fDAT=mult.dat
fMUL=valgrind_multiplica.dat
fTRAS=valgrind_traspuesta.dat

# Anadir valgrind y gnuplot al path
export PATH=$PATH:/share/apps/tools/valgrind/bin
# Indicar ruta librerías valgrind
export VALGRIND_LIB=/share/apps/tools/valgrind/lib/valgrind

rm -f $fDAT $fMUL $fTRAS

touch $fCACHE
touch $fMUL
touch $fTRAS

echo "Running slow and fast..."
# bucle para N desde P hasta Q
#for N in $(seq $Ninicio $Npaso $Nfinal);
for ((N = Ninicio ; N <= Nfinal ; N += Npaso)); do
	echo "N: $N / $Nfinal..."
	multiplicaValue=0
	traspuestaValue=0
  D1mr_mul=0
 	D1mr_tras=0
	D1mw_mul=0
  D1mw_tras=0

	# Varias ejecuciones para dismunir un posible error
	for ((i = 0; i < 15; i++)); do
		multiplicaTime1=$(./multiplica $N | grep 'time' | awk '{print $3}')
		multiplicaTime2=$(./multiplica $N + $Npaso | grep 'time' | awk '{print $3}')
		traspuestaTime1=$(./traspuesta $N | grep 'time' | awk '{print $3}')
		traspuestaTime2=$(./traspuesta $N + $Npaso | grep 'time' | awk '{print $3}')

		multiplicaValue=$(echo $multiplicaTime1 + $multiplicaValue | bc)
		traspuestaValue=$(echo $traspuestaTime1 + $traspuestaValue | bc)
	done

	multiplicaMedia=$(echo "scale=8; ($multiplicaValue / 15)" | bc)
	traspuestaMedia=$(echo "scale=8; ($traspuestaValue / 15)" | bc)
 
  # Indicamos las opciones por defecto de la maquina
  valgrind --tool=cachegrind --cachegrind-out-file=$fMUL ./multiplica $N
	cg_annotate $fMUL | head -n 30

  # Recogemos los valores de la ultima del fichero valgrind_slow.dat
  # que es donde salen los valores de lecturas producidas, ...
  mulRead=$(grep 'summary:' $fMUL | awk '{print $6}') # Cogemos D1mr
  mulWrite=$(grep 'summary:' $fMUL | awk '{print $9}') # Cogemos D1mw

  # Indicamos las opciones por defecto de la maquina
  valgrind --tool=cachegrind --cachegrind-out-file=$fTRAS ./traspuesta $N
  cg_annotate $fTRAS | head -n 30

  # Recogemos los valores de la ultima del fichero valgrind_fast.dat
  # que es donde salen los valores de lecturas producidas, ...
  trasRead=$(grep 'summary:' $fTRAS | awk '{print $6}')
  trasWrite=$(grep 'summary:' $fTRAS | awk '{print $9}')

  D1mr_mul=$(echo $mulRead + $D1mr_mul | bc)
  D1mw_mul=$(echo $mulWrite + $D1mw_mul | bc)
  D1mr_tras=$(echo $trasRead + $D1mr_tras | bc)
  D1mw_tras=$(echo $trasWrite + $D1mw_tras | bc)

	echo "$N	$multiplicaMedia $D1mr_mul $D1mw_mul $traspuestaMedia $D1mr_tras $D1mw_tras" >> $fDAT
done