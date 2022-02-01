/*
	Compilador final de PAUTLEN
	Miembros del grupo: Javier Fraile Igleisias,
						Jaime Diaz González,
						Iván Fernández París
*/

#ifndef _ALFA_H
#define _ALFA_H

#define MAX_LONG_ID 100
#define MAX_TAMANIO_VECTOR 64
#define TAM_TABLA_HASH 65536

/* DEFINES NECESARIOS */

/* Clase */
#define ESCALAR 1
#define VECTOR 2

/* Tipo */
#define INT 0
#define BOOLEAN 1

/* Categoria */
#define VARIABLE 1
#define PARAMETRO 2
#define FUNCION 3

/* FIN DEFINES NECESARIOS */

typedef enum
{
    OK = 1,
    ERR = 0
} STATUS;

#endif