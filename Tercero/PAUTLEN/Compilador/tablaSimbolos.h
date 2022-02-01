/*
	Compilador final de PAUTLEN
	Miembros del grupo: Javier Fraile Igleisias,
						Jaime Diaz González,
						Iván Fernández París
*/

#ifndef TABLASIMBOLOS_H
#define TABLASIMBOLOS_H

#include "tablaHash.h"

#define TABLA_SIMBOLOS_GLOBAL_TAM       65536
#define TABLA_SIMBOLOS_LOCAL_TAM        65536

typedef struct {
    TABLA_HASH *tabla_local;
    TABLA_HASH *tabla_global;
	int en_funcion;
} TABLA_SIMBOLO;

STATUS insertar(TABLA_SIMBOLO *tabla, INFO_SIMBOLO* desc_id);
STATUS insertarGlobal(TABLA_SIMBOLO *tabla, INFO_SIMBOLO* desc_id);
STATUS insertarLocal(TABLA_SIMBOLO *tabla, INFO_SIMBOLO* desc_id);
STATUS abrirAmbitoLocal(TABLA_SIMBOLO *tabla, INFO_SIMBOLO* desc_id);
STATUS cerrarAmbitoLocal();
INFO_SIMBOLO *buscarNodo (TABLA_SIMBOLO *tabla, const char *id);
INFO_SIMBOLO *buscarNodoEnLocal (TABLA_SIMBOLO *tabla, const char *id);
TABLA_SIMBOLO * init_tabla_simbolos ();
void free_tabla_simbolos (TABLA_SIMBOLO *tabla);

#endif /* TABLASIMBOLOS_H */
