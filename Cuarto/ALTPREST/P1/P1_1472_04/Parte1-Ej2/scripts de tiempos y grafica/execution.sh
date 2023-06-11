#!/bin/bash

# inicializar variables
Ninicio=100000
Npaso=100000
Nfinal=1000000

fDAT=times.dat

rm -f $fDAT

touch $fDAT

echo "Running times..."
for ((N = Ninicio ; N <= Nfinal ; N += Npaso)); do
  	echo "N: $N / $Nfinal..."
  	simple2Time=0
    	simple2_intrinsicTime=0

  	# Ejecutamos tanto simple como intrinseco para obtener el tiempo que tarda cada ejecucion
  	simple2Time=$(./simple2 $N | grep 'time' | awk '{print $3}')
  	simple2_intrinsicTime=$(./simple2_intrinsic $N | grep 'time' | awk '{print $3}')

	# Por cada tamaño guardamos el tiempo que ha tardado tanto en simple como en intrinsico
  	echo "$N $simple2Time	$simple2_intrinsicTime" >> $fDAT
done
