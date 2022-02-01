#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>

#include "arqo3.h"

void traspuesta(tipo **matrix1,tipo **tras,tipo **matriz3,int n);
void generarTraspuesta(tipo **matrix, tipo **tras,int n);

int main( int argc, char *argv[])
{
	int n;
	tipo **m1=NULL;
  tipo **m2=NULL;
  tipo **tras=NULL;
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
	if( !m1 )
	{
		return -1;
	}

  tras=generateEmptyMatrix(n);
	if( !tras )
	{
		return -1;
	}

  res=generateEmptyMatrix(n);
	if( !res )
	{
		return -1;
	}

	gettimeofday(&ini,NULL);

	/* Main computation */
	generarTraspuesta(m2, tras, n);
	traspuesta(m1, tras, res, n);
	/* End of computation */

	gettimeofday(&fin,NULL);
	printf("Execution time: %f\n", ((fin.tv_sec*1000000+fin.tv_usec)-(ini.tv_sec*1000000+ini.tv_usec))*1.0/1000000.0);

	free(m1);
  free(m2);
  free(tras);
  free(res);
	return 0;
}


void traspuesta(tipo **matrix1,tipo **tras,tipo **matriz3,int n)
{

	tipo result=0;
	int i,j; // indices nueva matriz
  int k; // indice aux
	
  for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			for(k=0; k<n; k++){
                result = matrix1[i][k]*tras[j][k];
                matriz3[i][j] += result;
            }
		}
	}
 
	return;
}

void generarTraspuesta(tipo **matrix, tipo **tras, int n)
{
  tipo value=0;
	int i,j; // indices nueva matriz

	for(i=0;i<n;i++)
	{
		for(j=0;j<n;j++)
		{
			value = matrix[j][i];
            tras[i][j] = value;
		}
	}
 
	return;
}
