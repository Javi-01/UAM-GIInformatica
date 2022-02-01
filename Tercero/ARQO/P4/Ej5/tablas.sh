# Integrantes: Javier Fraile Iglesias
#!/bin/bash
#
#$ -S /bin/bash
#$ -cwd
#$ -o salida_tablas.out
#$ -j y

./edgeDetectorSerie imagenes/SD.jpg

./edgeDetectorA3B3 imagenes/SD.jpg