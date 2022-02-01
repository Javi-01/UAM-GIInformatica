// ----------- Arqo P4-----------------------
// pescalar_par1
// 
// Integrantes: Javier Fraile Iglesias
// ¿Funciona correctamente?
//
#include <omp.h>
#include <stdio.h>
#include <stdlib.h>
#include "arqo4.h"

int main(void)
{
	int nproc, n_p_thread;
	float *A=NULL, *B=NULL;
	long long k=0;
	struct timeval fin,ini;
	double sum=0;

	A = generateVectorOne(M);
	B = generateVectorOne(M);
	if ( !A || !B )
	{
		printf("Error when allocationg matrix\n");
		freeVector(A);
		freeVector(B);
		return -1;
	}
	
        nproc=omp_get_num_procs();
        omp_set_num_threads(nproc);   
     
        printf("Se han lanzado %d hilos.\n",nproc);

	gettimeofday(&ini,NULL);
	/* Bloque de computo */
	sum = 0;
	n_p_thread=M/nproc;
	printf("Work per thread %d\n",n_p_thread);
    #pragma omp parallel for shared(A,B) private(k) schedule(static, n_p_thread)
	for(k=0;k<M;k++)
	{
		sum = sum + A[k]*B[k];
		printf("Thread %d worked on element: %lld\n", omp_get_thread_num(), k);
	}
	/* Fin del computo */
	gettimeofday(&fin,NULL);

	printf("Resultado: %f\n",sum);
	printf("Tiempo: %f\n", ((fin.tv_sec*1000000+fin.tv_usec)-(ini.tv_sec*1000000+ini.tv_usec))*1.0/1000000.0);
	freeVector(A);
	freeVector(B);

	return 0;
}
