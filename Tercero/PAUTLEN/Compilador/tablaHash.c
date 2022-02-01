/*
	Compilador final de PAUTLEN
	Miembros del grupo: Javier Fraile Igleisias,
						Jaime Diaz González,
						Iván Fernández París
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tablaHash.h"

INFO_SIMBOLO *init_info_symbol(char *lexema, int categoria, int clase, int tipo, int tamano, int numero_variables_locales, int posicion_variable_local, int numero_parametros, int posicion_parametro)
{

    INFO_SIMBOLO *symbol = NULL;

    // Inicializamos el puntero a symbolo
    symbol = (INFO_SIMBOLO *)malloc(sizeof(INFO_SIMBOLO));
    if (!symbol)
    {
        return NULL;
    }

    // Asignamos los valores
    symbol->categoria = categoria;
    symbol->clase = clase;
    strcpy(symbol->lexema, lexema);
    symbol->num_parametros = numero_parametros;
    symbol->num_variables_locales = numero_variables_locales;
    symbol->posicion_parametro = posicion_parametro;
    symbol->tamano = tamano;
    symbol->tipo = tipo;

    return symbol;
}

Nodo *init_nodo(INFO_SIMBOLO *info)
{
    if (info == NULL)
        return NULL;

    Nodo *nodo = NULL;

    nodo = (Nodo *)malloc(sizeof(Nodo));
    if (!nodo)
    {
        return NULL;
    }

    nodo->info_simbolo_nodo = info;
    nodo->nodo_siguiente = NULL;

    return nodo;
}

TABLA_HASH *init_tabla(int tam)
{
    TABLA_HASH *tabla = NULL;
    int i;

    if (tam < 1)
        return NULL;

    tabla = (TABLA_HASH *)malloc(sizeof(TABLA_HASH));
    if (tabla == NULL)
    {
        return NULL;
    }

    tabla->tabla_hash = (Nodo **)malloc(sizeof(Nodo *) * tam);
    if (tabla->tabla_hash == NULL)
    {
        return NULL;
    }

    for (i = 0; i < tam; i++)
    {
        tabla->tabla_hash[i] = NULL;
    }

    tabla->tamano = tam;

    return tabla;
}

void free_info_symbol(INFO_SIMBOLO *info)
{
    if (!info)
    {
        return;
    }

    free(info);
}

void free_nodo(Nodo *nodo)
{
    if (!nodo)
    {
        return;
    }

    free(nodo);
}

void free_tabla(TABLA_HASH *tabla)
{
    int i;

    if (!tabla)
    {
        return;
    }

    for (i = 0; i < tabla->tamano; i++)
    {
        free_nodo(tabla->tabla_hash[i]);
    }

    free(tabla->tabla_hash);
    free(tabla);
}

unsigned long hash(TABLA_HASH *tabla, const char *cad, int len_cad)
{
    if (tabla == NULL)
        return ERR;

    unsigned int hash = 1315423911;
    unsigned int i = 0;

    for (i = 0; i < len_cad; i++)
    {
        hash ^= ((hash << 5) + (*cad) + (hash >> 2));
    }

    return hash % tabla->tamano;
}

INFO_SIMBOLO *obtener_valor(TABLA_HASH *tabla, const char *identificador)
{
    if (tabla == NULL)
        return ERR;

    unsigned long hash_value = 0;
    Nodo *nodo;

    hash_value = hash(tabla, identificador, strlen(identificador));
    nodo = tabla->tabla_hash[hash_value];

    // Comprobamos si estamos en el nodo adecuado
    while (nodo != NULL && nodo->info_simbolo_nodo != NULL && strcmp(identificador, nodo->info_simbolo_nodo->lexema) > 0)
    {
        nodo = nodo->nodo_siguiente;
    }

    // Si no encontramos al nodo adecuado
    if (nodo == NULL || nodo->info_simbolo_nodo == NULL || strcmp(identificador, nodo->info_simbolo_nodo->lexema) != 0)
    {
        return NULL;
    }
    else
    {
        // Cuando ya tenemos el nodo adecuado devolvemos su value asignado
        return nodo->info_simbolo_nodo;
    }
}

STATUS insertar_nodo(TABLA_HASH *tabla, Nodo *nodo)
{

    if (tabla == NULL || nodo == NULL)
        return ERR;

    unsigned long hash_value = 0;
    Nodo *nodo_actual = NULL;
    Nodo *nodo_siguiente = NULL;

    hash_value = hash(tabla, nodo->info_simbolo_nodo->lexema, strlen(nodo->info_simbolo_nodo->lexema));
    nodo_actual = tabla->tabla_hash[hash_value];

    // // Recorremos la tabla hasta encontrar el último nodo insertado
    while (nodo_actual != NULL && nodo_actual->info_simbolo_nodo != NULL &&
           strcmp(nodo->info_simbolo_nodo->lexema, nodo_actual->info_simbolo_nodo->lexema) > 0)
    {
        nodo_siguiente = nodo_actual;
        nodo_actual = nodo_siguiente->nodo_siguiente;
    }

    // Comprobamos que el nodo a insetar no estuviera ya insertado previamente
    if (nodo_actual != NULL && nodo_actual->info_simbolo_nodo != NULL &&
        strcmp(nodo->info_simbolo_nodo->lexema, nodo_actual->info_simbolo_nodo->lexema) == 0)
    {
        return ERR;
    }
    else
    { // En caso de no estar ya insertado, lo insertamos
        if (nodo_actual == tabla->tabla_hash[hash_value])
        {
            nodo->nodo_siguiente = nodo_actual;
            tabla->tabla_hash[hash_value] = nodo;
        }
        else if (nodo_actual == NULL)
        {
            nodo_siguiente->nodo_siguiente = nodo;
        }
        else
        {
            nodo->nodo_siguiente = nodo_actual;
            nodo_siguiente->nodo_siguiente = nodo;
        }
    }

    return OK;
}