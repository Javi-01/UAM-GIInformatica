/*
	Compilador final de PAUTLEN
	Miembros del grupo: Javier Fraile Igleisias,
						Jaime Diaz González,
						Iván Fernández París
*/

#include "alfa.h"
#include "y.tab.h"
#include <stdio.h>
#include <stdlib.h>

int main (int argc, char **argv){
  extern FILE *yyin;
  extern FILE *yyout;

  if (argc < 3){
    printf("\nError en los argumentos de entrada");

    return -1;
  }

  yyin = fopen(argv[1], "r");
  yyout = fopen(argv[2], "w");

  if(yyparse() != 0){
    printf("Error en el compilador\n");
  } else {
      printf("Compilacion correcta\n");
  }

  fclose(yyout);
  fclose(yyin);

  return 0;
}