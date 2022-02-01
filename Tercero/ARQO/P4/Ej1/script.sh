# Integrantes: Javier Fraile Iglesias
#!/bin/bash
#
#$ -S /bin/bash
#$ -cwd
#$ -o salida.out
#$ -j y

export OMP_NUM_THREADS=16

./omp1