/**
 * @brief Modulo encargado del inventario del jugador
 *
 * @file inventory.c
 * @author José Miguel Nicolás García 
 * @version 1.0
 * @date 24-03-2019
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include "inventory.h"
#include "set.h"
#include "types.h"

struct _BackPack /*!< BackPack structure*/
{
    int maxObj; /*!< Máximo número de elementos */
    Set *set;   /*!< Conjunto de elementos */
};

BackPack *inventory_create()
{
    BackPack *newBackPack = NULL;

    newBackPack = (BackPack *)malloc(sizeof(BackPack));

    newBackPack->set = set_create();
    newBackPack->maxObj = UNDIFINED_SIZE;

    return newBackPack;
}

void inventory_destroy(BackPack *backPack)
{
    if (backPack == NULL)
    {
        return;
    }

    set_destroy(backPack->set);
    free(backPack);
}

STATUS inventory_add_object(BackPack *backPack, Id newObject)
{
    if (backPack == NULL || newObject == NO_ID || inventory_num_obj(backPack) == inventory_get_maxObj(backPack))
    {
        return ERROR;
    }

    return set_add(backPack->set, newObject);
}

STATUS inventory_remove_object(BackPack *backPack, Id id)
{
    if (backPack == NULL || id == NO_ID)
    {
        return ERROR;
    }

    if (inventory_have_id(backPack, id) == TRUE)
    {
        return set_del(backPack->set, id);
    }

    return FALSE;
}
//getters
Id inventory_get_object(BackPack *backPack, int position)
{
    if (backPack == NULL || position < 0 || position > backPack->maxObj)
    {
        return NO_ID;
    }

    return set_get_id(backPack->set, position);
}

Id inventory_get_last_object(BackPack *backPack)
{
    if (backPack == NULL)
    {
        return NO_ID;
    }

    return set_get_last_id(backPack->set);
}
Id *inventory_get_objects(BackPack *backPack)
{
    if (!backPack)
    {
        return NULL;
    }

    return set_get_ids(backPack->set);
}

int inventory_get_maxObj(BackPack *backPack)
{
    if (backPack == NULL)
    {
        return UNDIFINED_SIZE;
    }

    return backPack->maxObj;
}

Set *inventory_get_set(BackPack *backPack)
{
    if (backPack == NULL)
    {
        return NULL;
    }

    return backPack->set;
}

//Setters

STATUS inventory_set_maxObj(BackPack *backPack, int maxObj)
{
    if (backPack == NULL || maxObj < 0)
    {
        return ERROR;
    }

    backPack->maxObj = maxObj;
    return OK;
}
/*
STATUS inventory_set_set(BackPack *backPack, Set *set)
{
    if (backPack == NULL)
    {
        return ERROR;
    }

    backPack->set = set;
    return OK;
}*/

//Cantidad actual objetos
int inventory_num_obj(BackPack *backPack)
{
    if (backPack == NULL)
    {
        return UNDIFINED_SIZE;
    }

    return set_get_cantidad(backPack->set);
}

void inventory_print(BackPack *backPack)
{
    printf("\n BACKPACK WHIT %d ELEMENTS: \n", inventory_get_maxObj(backPack));
    set_print(backPack->set);
    printf("\n");
}

BOOL inventory_have_id(BackPack *backPack, Id object)
{
    return set_have_id(backPack->set, object);
}