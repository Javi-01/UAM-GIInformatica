#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>

#include "arqo3.h"

void multiplicacion(tipo **matrix1,tipo **matrix2,tipo **matrix3,int n);

int main( int argc, char *argv[])
{
	int n;
	tipo **m1=NULL;
  tipo **m2=NULL;
  tipo **res=NULL;
	struct timeval fin,ini;

	printf("Word size: %ld bits\n",8*sizeof(tipo));

	if( argc!=2 )
	{
		printf("Error: ./%s <matrix size>\n", argv[0]);
		return -1;
	}
 
	n=atoi(argv[1]);
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

	tipo result=0;
	int i,j; // indices nueva matriz
  int k; // indice aux

	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			for(k=0; k<n; k++){
                result = matrix1[i][k]*matrix2[k][j];
                matrix3[i][j] += result;
            }
		}
	}
 
	return;
}
