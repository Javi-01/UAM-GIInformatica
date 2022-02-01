/*
	Compilador final de PAUTLEN
	Miembros del grupo: Javier Fraile Igleisias,
						Jaime Diaz González,
						Iván Fernández París
*/

#ifndef TABLAHASH_H
#define TABLAHASH_H

#include "alfa.h"

typedef struct
{
    char lexema [MAX_LONG_ID+1];
    int categoria;
    int clase;
    int tipo;
    int tamano;
    int num_variables_locales;
    int posicion_variable_local;
    int num_parametros;
    int posicion_parametro;
} INFO_SIMBOLO;

typedef struct nodo
{
    INFO_SIMBOLO *info_simbolo_nodo;
    struct nodo *nodo_siguiente;
} Nodo;

typedef struct
{
    int tamano;
    Nodo **tabla_hash;
} TABLA_HASH;


INFO_SIMBOLO * init_info_symbol(char *lexema, int categoria, int clase, int tipo, int tamano, int numero_variables_locales, int posicion_variable_local, int numero_parametros, int posicion_parametro);
Nodo * init_nodo (INFO_SIMBOLO *info);
TABLA_HASH * init_tabla (int tam);
void free_info_symbol(INFO_SIMBOLO *info);
void free_nodo (Nodo *nodo);
void free_tabla (TABLA_HASH *tabla);
unsigned long hash(TABLA_HASH *tabla, const char *cad, int len_cad);
INFO_SIMBOLO *obtener_valor (TABLA_HASH *tabla, const char *identificador);
STATUS insertar_nodo (TABLA_HASH *tabla, Nodo *nodo);


#endif /* TABLAHASH_H */
