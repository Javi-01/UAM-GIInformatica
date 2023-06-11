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

  	# Varias ejecuciones para dismunir un posible error
  	simple2Time=$(./simple2 $N | grep 'time' | awk '{print $3}')
  	simple2_intrinsicTime=$(./simple2_intrinsic $N | grep 'time' | awk '{print $3}')

  	echo "$N $simple2Time	$simple2_intrinsicTime" >> $fDAT
done
