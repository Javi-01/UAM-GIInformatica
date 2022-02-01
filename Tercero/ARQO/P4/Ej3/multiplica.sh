# Integrantes: Javier Fraile Iglesias

#!/bin/bash

# inicializar variables
Ninicio=1015
Npaso=64
Nfinal=1539

fDAT=mult.dat

NHilosTotal=4

rm -f $fDAT

touch $fDAT

echo "Running multiplica..."
for ((N = Ninicio ; N <= Nfinal ; N += Npaso)); do
  	echo "N: $N / $Nfinal..."
    echo "Num hilos: $NHilosTotal..."
  	multiplicaValue_serie=0
    multiplicaValue_3=0
    multiplicaMediaTime_serie=0
    multiplicaMediaTime_3=0
    aceleracion=0
  
  	# Varias ejecuciones para dismunir un posible error
  	for ((i = 0; i < 10; i++)); do
  		multiplicaTime_serie=$(./multiplica_serie $N | grep 'time' | awk '{print $3}')
  		multiplicaTime_3=$(./multiplica3 $N $NHilosTotal | grep 'time' | awk '{print $3}')
  
  		multiplicaValue_serie=$(echo "scale=8; ($multiplicaTime_serie + $multiplicaValue_serie)" | bc)
      multiplicaValue_3=$(echo "scale=8; ($multiplicaTime_3 + $multiplicaValue_3)" | bc)
  	done
  
  	multiplicaMediaTime_serie=$(echo "scale=8; ($multiplicaValue_serie / 10)" | bc)
    multiplicaMediaTime_3=$(echo "scale=8; ($multiplicaValue_3 / 10)" | bc)
    
    aceleracion=$(echo "scale=8; ($multiplicaMediaTime_serie / $multiplicaMediaTime_3)" | bc)
  
  	echo "$N $NHilosTotal	$multiplicaMediaTime_serie $multiplicaMediaTime_3 $aceleracion" >> $fDAT
done