/*
	Compilador final de PAUTLEN
	Miembros del grupo: Javier Fraile Igleisias,
						Jaime Diaz González,
						Iván Fernández París
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tablaSimbolos.h"

// Identificar que tipo de Identificador se quiere insertar en cuanto a variables
STATUS insertar(TABLA_SIMBOLO *tabla, INFO_SIMBOLO *desc_id)
{
   if (desc_id == NULL || tabla == NULL)
   {
      return ERR;
   }

   // Comprobamos si estamos en un ambito local o global
   if (tabla->tabla_local == NULL)
   {
      return insertarGlobal(tabla, desc_id);
   }
   else
   {
      return insertarLocal(tabla, desc_id);
   }
}

STATUS insertarGlobal(TABLA_SIMBOLO *tabla, INFO_SIMBOLO *desc_id)
{

   if (desc_id == NULL || tabla == NULL)
   {
      return ERR;
   }

   return insertar_nodo(tabla->tabla_global, init_nodo(desc_id));
}

STATUS insertarLocal(TABLA_SIMBOLO *tabla, INFO_SIMBOLO *desc_id)
{

   if (desc_id == NULL || tabla == NULL)
   {
      return ERR;
   }

   return insertar_nodo(tabla->tabla_local, init_nodo(desc_id));
}

// Identificar que tipo de Identificador se quiere insertar en cuanto a funciones
STATUS abrirAmbitoLocal(TABLA_SIMBOLO *tabla, INFO_SIMBOLO *desc_id)
{
   STATUS ret;

   if (desc_id == NULL || tabla == NULL)
   {
      return ERR;
   }

   // Si no se ha declarado un ambito local previamente
   // Insertamos la funcion en el ambito global y abrimos un ambito local
   if (tabla->en_funcion == 0)
   {
      // Sino hay ambito local, insetamos la funcion en el ambito global
      ret = insertarGlobal(tabla, desc_id);
      if (ret == ERR)
      {
         return ret;
      }

      // Si no hay un ambito local creado previamente, lo creamos
      tabla->tabla_local = init_tabla(TABLA_SIMBOLOS_LOCAL_TAM);
      if (tabla->tabla_local == NULL)
      {
         return ERR;
      }

      // Insertamos el simbolo en el ambito local
      return insertarLocal(tabla, desc_id);
   }
   else // Si no pues hay fallo de insercion
   {
      return ERR;
   }
}

STATUS cerrarAmbitoLocal(TABLA_SIMBOLO *tabla)
{
   // Borramos el ambito local si esta creado
   if (tabla->tabla_local != NULL)
   {
      // free(tabla->tabla_local);
      tabla->tabla_local = NULL;
      tabla->en_funcion = 0;
      return OK;
   }
   return ERR;
}

INFO_SIMBOLO *buscarNodo(TABLA_SIMBOLO *tabla, const char *id)
{
   // Si hay un ambito local abierto primeros se busca el simbolo ahi
   if (tabla->tabla_local != NULL)
   {
      if (obtener_valor(tabla->tabla_local, id) != NULL)
      {
         return obtener_valor(tabla->tabla_local, id);
      }
   }

   // Si no se busca en el ambito global
   return obtener_valor(tabla->tabla_global, id);
}

INFO_SIMBOLO *buscarNodoEnLocal(TABLA_SIMBOLO *tabla, const char *id)
{
   // Solo buscamos en el ambito local
   if (tabla->tabla_local != NULL)
   {
      if (obtener_valor(tabla->tabla_local, id) != NULL)
      {
         return obtener_valor(tabla->tabla_local, id);
      }
   }

   return NULL;
}

TABLA_SIMBOLO *init_tabla_simbolos()
{

   TABLA_SIMBOLO *tabla = NULL;

   tabla = (TABLA_SIMBOLO *)malloc(sizeof(TABLA_SIMBOLO));
   if (tabla == NULL)
   {
      return NULL;
   }

   tabla->tabla_global = init_tabla(TABLA_SIMBOLOS_GLOBAL_TAM);
   if (tabla->tabla_global == NULL)
   {
      free_tabla_simbolos(tabla);
      return NULL;
   }

   tabla->tabla_local = NULL;
   tabla->en_funcion = 0;

   return tabla;
}

void free_tabla_simbolos(TABLA_SIMBOLO *tabla)
{

   if (tabla->tabla_local != NULL)
   {
      free_tabla(tabla->tabla_local);
   }

   if (tabla->tabla_global != NULL)
   {
      free_tabla(tabla->tabla_global);
   }

   free(tabla);

   return;
}