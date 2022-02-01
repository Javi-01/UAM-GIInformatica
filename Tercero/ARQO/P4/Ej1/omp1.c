// ----------- Arqo P4-----------------------
// Programa que crea hilos utilizando OpenMP
// 
// Integrantes: Javier Fraile Iglesias
//
#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

#define OMP_NUM_THREADS 16
int num_threads = 16;

int main(int argc, char *argv[])
{
	int tid,nthr,nproc, numthr = 16;
	nproc = omp_get_num_procs();
	printf("Hay %d cores disponibles\n", nproc); 
  
  printf("Prueba para OMP_NUM_THREADS con 16 hilos\n");
	#pragma omp parallel private (tid)
	{
		tid = omp_get_thread_num();
		nthr = omp_get_num_threads();
		printf("Hola, soy el hilo %d de %d\n", tid, nthr);
	} 
  
  printf("-----------------------------------------------\n");
  omp_set_num_threads(num_threads);
	printf("Prueba para omp_set_num_threads(int num_threads) con %d hilos\n", num_threads);
	#pragma omp parallel private(tid)
	{
		tid = omp_get_thread_num();
		nthr = omp_get_num_threads();
		printf("Hola, soy el hilo %d de %d\n", tid, nthr);
	}
 
  printf("-----------------------------------------------\n");
  printf("Prueba para #pragma omp parallel num_threads(numthr) con %d hilos\n", numthr);
	#pragma omp parallel num_threads(numthr) private (tid)
	{
		tid = omp_get_thread_num();
		nthr = omp_get_num_threads();
		printf("Hola, soy el hilo %d de %d\n", tid, nthr);
	}
 
	return 0;
}
