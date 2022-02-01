/**
 * @brief Implementación de la estructura de conjuntos y todas sus funciones
 *
 * @file set.c
 * @author José Miguel Nicolás García 
 * @version 1.0
 * @date 09-03-2021
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "object.h"
#include "game.h"
#include "space.h"
#include "types.h"
#include "set.h"

/*!< Set structure*/
struct _Set
{

    int cantidad;    /*!< Cantidad de datos del conjunto */
    Id ids[NUM_IDs]; /*!< Array con todos los identificadores del conjunto. */
};

/*Funciones privadas*/

/**
* @brief Comprueba que el id esté ya en el conjunto. Un conjunto no debe tener datos repetidos.
* @param set puntero al conjunto.
* @param id id a comprobar.
* @return  ERROR en caso de que no esté o OK contrario.
* @author José Miguel Nicolás García
*/
BOOL set_have_id(Set *set, Id id);

Set *set_create()
{
    Set *newSet = NULL;
    int i = 0;

    //Inicializamos espacio
    if (!(newSet = (Set *)malloc(sizeof(Set))))
    {
        return NULL;
    }
    //Ponemos rellenamos IDs por defecto
    for (i = 0; i < NUM_IDs; i++)
    {
        newSet->ids[i] = NO_ID;
    }
    //Y ponemos la cantidad a 0
    newSet->cantidad = 0;
    return newSet;
}

STATUS set_destroy(Set *set)
{
    if (!set)
    {
        return ERROR;
    }
    free(set);

    return OK;
}

STATUS set_add(Set *set, Id newId)
{
    //Si los parámetros no son correctos o se excede la longitud error

    if (!set || !newId || set->cantidad == NUM_IDs)
    {
        return ERROR;
    }

    //Un conjunto nunca puede tener dos datos iguales
    if (set_have_id(set, newId))
    {
        return OK;
    }

    set->ids[set->cantidad] = newId;
    set->cantidad++;

    return OK;
}

BOOL set_have_id(Set *set, Id id)
{
    int i = 0;
    if (!set || !id)
    {
        return FALSE;
    }
    for (i = 0; i < set->cantidad; i++)
    {
        if (set->ids[i] == id)
        {
            return TRUE;
        }
    }

    return FALSE;
}
int set_get_cantidad(Set *set)
{
    if (set == NULL)
    {
        return -1;
    }

    return set->cantidad;
}

STATUS set_del(Set *set, Id id)
{
    int i = 0, j = 0;
    if (!set || !id)
    {
        return ERROR;
    }
    if (set->cantidad == 0)
    {
        return ERROR;
    }
    for (i = 0; i < set->cantidad; i++)
    { //Retrocedemos todas las posiciones del array a partir del eliminado
        if (set->ids[i] == id)
        {
            for (j = i + 1; j < set->cantidad; i++, j++)
            {
                set->ids[i] = set->ids[j];
            }
            set->cantidad--;
            set->ids[set->cantidad] = NO_ID;
        }
    }
    return OK;
}

STATUS set_print(Set *set)
{
    int i = 0;
    if (!set)
    {
        return ERROR;
    }

    fprintf(stdout, "Set de %d datos: \n", set->cantidad);
    for (i = 0; i < set->cantidad; i++)
    {
        fprintf(stdout, "-Id [%d]: %ld\n", i, set->ids[i]);
    }
    return OK;
}

Id set_get_id(Set *set, int pos)
{
    if (!set || pos < 0 || pos >= NUM_IDs)
    {
        return NO_ID;
    }
    return set->ids[pos];
}
Id set_get_last_id(Set *set)
{
    return set_get_id(set, set->cantidad - 1);
}

Id *set_get_ids(Set *set)
{
    if (!set)
    {
        return NULL;
    }

    return set->ids;
}
