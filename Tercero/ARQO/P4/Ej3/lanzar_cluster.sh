# Integrantes: Javier Fraile Iglesias
#!/bin/bash
#
#$ -S /bin/bash
#$ -cwd
#$ -o salida.out
#$ -j y

# Pasamos el nombre del script como parámetro
echo "Ejecutando script $1…"
echo ""
source $1
