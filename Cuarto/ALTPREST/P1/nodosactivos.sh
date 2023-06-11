#!/bin/bash

array1=()
command1=$(qstat -f | awk 'NR>1{print $1 $6}')

# Vemos y guardamos el state y la arquitectura de los nodos
# ya que si el state es "" no lo guarda para recorrer
for v in $command1
do
	if [[ "$v" == *"all."* ]]
	then
		array1+=($v)
	fi
done

numNodos=0
for v in "${array1[@]}"
do
	numNodos=$((numNodos+1))
done

numNodosNoActivos=0
for v in "${array1[@]}"
do
	if [[ "$v" == *"au"* ]]
	then
		numNodosNoActivos=$((numNodosNoActivos+1))
	fi
done

numNodosActivos=$(($numNodos-$numNodosNoActivos))

echo " Numero de nodos = $numNodos"
echo " Numero de nodos no activos = $numNodosNoActivos" 
echo "Porcentaje de nodos activos = $numNodosActivos/$numNodos"
echo ""

array2=()
command2=$(rocks list host | awk '{print $1}')

# Obtenemos y guardamos el nombre de los nodos
i=0
for v in $command2
do
	if [[ $i > 1 ]]
	then
		length=${#v}
		parser=${v::length-1}
		array2+=($parser)	
	fi
	i=$((i+1))
done

echo " Listado de Nodos"

# Imprimos cada nodo, su ip y si esta activo o no
for (( i=0; i<$numNodos; i++ ))
do
	
	# Hacemos un PING a cada nodo y parseamos para obtener su IP	
	command3=$(ping -c1 ${array2[$i]} | cut -d '(' -f 2 | cut -d ')' -f 1 | cut -d ' ' -f 2)
	for ip in $command3
	do
		break
	done

	# Comprobamos si en la lista de estados
	# las cadenas guardadas contiene au (no activo) en su nombre
	if [[ ${array1[$i]} == *"au"* ]]
	then
		echo "${array2[$i]}	$ip	NO ACTIVO"
	else
		echo "${array2[$i]}	$ip 	ACTIVO"
	fi
done
