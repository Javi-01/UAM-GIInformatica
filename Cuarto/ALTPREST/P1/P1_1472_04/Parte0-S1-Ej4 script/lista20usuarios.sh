#!/bin/bash

for (( i=1; i<=20; i++ ))
do
	# Creamos el nombre de usuario
	if [[ $i -lt 10 ]]
	then
		username="usuBD0$i"
	else
		username="usuBD$i"
	fi

	# Creamos una contraseña con el
	# username concatenado con un numero
	# random entre 1.000 y 100.000 
	password="$username$(($RANDOM%100000 + 1000))"

	# Imprimimos los usuarios y los guardamos en un fichero auxiliar
	echo "usuario $username:$password"	
	echo "$username:$password" >> lista20usuarios.txt
	
	# Anadimos el usuario
	useradd $username
done

# Una vez creados los usuarios
# anadimos las password a cada uno en lote
# estando las contraseñas en el fichero datos.txt
chpasswd < lista20usuarios.txt
