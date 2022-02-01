// ----------- Arqo P4-----------------------
// 
// Integrantes: Javier Fraile Iglesias

#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <sys/time.h>

#include "arqo3.h"

void multiplicacion(tipo **matrix1,tipo **matrix2,tipo **matrix3,int n);

int main( int argc, char *argv[])
{
	int n, nproc;
	tipo **m1=NULL;
  tipo **m2=NULL;
  tipo **res=NULL;
	struct timeval fin,ini;
 
  printf("Word size: %ld bits\n",8*sizeof(tipo)); 

	if( argc!=3 )
	{
		printf("Error: ./%s <matrix size> <num threads>\n", argv[0]);
		return -1;
	}
 
	n=atoi(argv[1]);
  nproc=atoi(argv[2]); 
  omp_set_num_threads(nproc); // Indicamos el numero de hilos a usar
	m1=generateMatrix(n);
	if( !m1 )
	{
		return -1;
	}

  m2=generateMatrix(n);
	if( !m2 )
	{
		return -1;
	}

  res=generateEmptyMatrix(n);
	if( !res )
	{
		return -1;
	}


  // Calculamos el tiempo para la multiplicacion
	gettimeofday(&ini,NULL);

	/* Main computation */
	multiplicacion(m1, m2, res, n);
	/* End of computation */

	gettimeofday(&fin,NULL);
	printf("Execution time: %f\n", ((fin.tv_sec*1000000+fin.tv_usec)-(ini.tv_sec*1000000+ini.tv_usec))*1.0/1000000.0);

	free(m1);
  free(m2);
  free(res);
	return 0;
}


void multiplicacion(tipo **matrix1,tipo **matrix2,tipo **matrix3,int n)
{

	int i,j; // indices nueva matriz
  int k; // indice aux

  #pragma omp parallel for private(i, j, k) shared(matrix1,matrix2,matrix3) schedule(static)
	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			for(k=0; k<n; k++){
                matrix3[i][j] += matrix1[i][k]*matrix2[k][j];
            }
		}
	}
 
	return;
}
