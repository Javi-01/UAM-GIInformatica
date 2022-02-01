# Integrantes: Javier Fraile Iglesias e Iván Fernández París

#!/bin/bash

# inicializar variables
#Como somos la pareja 10, el valor de P será 3 ya que es el valor de “10 mod 7”.
Ninicio=1024+1024*3
Npaso=256
Nfinal=1024+1024*4

TAMcacheinicio=8192
TAMpaso=8192
TAMcachefinal=8192
Ltam=8388608

Nvias=1
TAMlinea=64

fSLOW=valgrind_slow.dat
fFAST=valgrind_fast.dat
fCACHE=cache_8192.dat

# Anadir valgrind y gnuplot al path
export PATH=$PATH:/share/apps/tools/valgrind/bin
# Indicar ruta librerías valgrind
export VALGRIND_LIB=/share/apps/tools/valgrind/lib/valgrind

# generar el fichero DAT vacío
touch $fCACHE

rm -f $fSLOW $fFAST

touch $fFAST
touch $fSLOW

echo "Running slow and fast..."
# bucle para N desde P hasta Q 
#for N in $(seq $Ninicio $Npaso $Nfinal);

# Preguntar acerca de si 1 por configuracion se refiere a el TAM o al bucle for de 0 hasta 15
for TAM in $(seq $TAMcacheinicio $TAMpaso $TAMcachefinal); do # Para solo hacer un ciclo por tamaño de de cache 
	for ((N = Ninicio ; N <= Nfinal ; N += Npaso)); do
		echo "TAM cache: $TAM | N: $N / $Nfinal..."
		D1mr_slow=0
		D1mr_fast=0
		D1mw_slow=0
		D1mw_fast=0

		
		# Indicamos las opciones de I1, D1 y LL con su size, asociatividad (siempre 1) y tamaño de linea (64)
		valgrind --tool=cachegrind --I1=$TAM,$Nvias,$TAMlinea  --D1=$TAM,$Nvias,$TAMlinea --LL=$Ltam,$Nvias,$TAMlinea --cachegrind-out-file=$fSLOW ./slow $N
		cg_annotate $fSLOW | head -n 30

		# Recogemos los valores de la ultima del fichero valgrind_slow.dat
		# que es donde salen los valores de lecturas producidas, ...
		slowRead=$(grep 'summary:' $fSLOW | awk '{print $6}') # Cogemos D1mr
		slowWrite=$(grep 'summary:' $fSLOW | awk '{print $9}') # Cogemos D1mw
		# ¿EJECUTAR DE NUEVO VALGRIND?

		# Indicamos las opciones de I1, D1 y LL con su size, asociatividad (siempre 1) y tamaño de linea (64)
		valgrind --tool=cachegrind --I1=$TAM,$Nvias,$TAMlinea  --D1=$TAM,$Nvias,$TAMlinea --LL=$Ltam,$Nvias,$TAMlinea --cachegrind-out-file=$fFAST ./fast $N
		cg_annotate $fFAST | head -n 30

		# Recogemos los valores de la ultima del fichero valgrind_fast.dat
		# que es donde salen los valores de lecturas producidas, ...
		fastRead=$(grep 'summary:' $fFAST | awk '{print $6}')
		fastWrite=$(grep 'summary:' $fFAST | awk '{print $9}')
		# ¿EJECUTAR DE NUEVO VALGRIND?

		D1mr_slow=$(echo "scale=8; ($slowRead + $D1mr_slow)" | bc)
		D1mw_slow=$(echo "scale=8; ($slowWrite + $D1mw_slow)" | bc)
		D1mr_fast=$(echo "scale=8; ($fastRead + $D1mr_fast)" | bc)
		D1mw_fast=$(echo "scale=8; ($fastWrite + $D1mw_fast)" | bc)

		echo "$N	$D1mr_slow $D1mw_slow $D1mr_fast $D1mw_fast" >> $fCACHE
	done
done
