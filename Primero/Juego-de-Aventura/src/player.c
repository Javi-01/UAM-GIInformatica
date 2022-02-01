/**
 * @brief Implementación de la estructura de objetos y sus funciones.
 *
 * @file player.c
 * @author Alejandro Jerez 
 * @version 1.0
 * @date 18-02-2021
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "player.h"
#include "types.h"
#include "space.h"
#include "inventory.h"
#include "object.h"

struct _Player /*!< Player structure*/
{
  Id id;                    /*!< Identificador del jugador */
  char name[WORD_SIZE + 1]; /*!< Nombre del jugador */
  Id location;              /*!< Identificador de la localización del jugador */
  BackPack *backPack;       /*!< Inventario del jugador */
  Id last_object_id;        /*!< Identificador del último objeto */
};

/*Funciones privadas*/

/**
* @brief Cambia el id de un jugador por el id recibido.
* @param player puntero al jugador.
* @param id nuevo id.
* @return ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS player_set_id(Player *player, Id id);

Player *player_create(Id id)
{

  Player *newPlayer = NULL;

  if (id == NO_ID)
  {
    return NULL;
  }

  newPlayer = (Player *)malloc(sizeof(Player));

  if (newPlayer == NULL)
  {
    return NULL;
  }

  newPlayer->id = id;
  newPlayer->location = NO_ID;
  newPlayer->last_object_id = NO_ID;
  newPlayer->name[0] = '\0';
  newPlayer->backPack = inventory_create();
  if (newPlayer->backPack == NULL)
  {
    return NULL;
  }

  return newPlayer;
}

STATUS player_destroy(Player *player)
{

  if (!player)
  {
    return ERROR;
  }
  inventory_destroy(player->backPack);
  free(player);
  player = NULL;

  return OK;
}

Id player_get_id(Player *player)
{

  if (!player)
  {
    return NO_ID;
  }
  return player->id;
}

const char *player_get_name(Player *player)
{

  if (!player)
  {
    return NULL;
  }

  return player->name;
}

Id player_get_location(Player *player)
{

  if (!player)
  {
    return NO_ID;
  }

  return player->location;
}

int player_get_num_of_objects(Player *player)
{

  if (!player)
  {
    return -1;
  }

  return inventory_num_obj(player->backPack);
}

Id *player_get_objects(Player *player)
{

  if (!player)
  {
    return NULL;
  }

  return inventory_get_objects(player->backPack);
}

Id player_get_last_object(Player *player)
{

  if (!player)
    return NO_ID;

  return player->last_object_id;
}

STATUS player_set_max_inv(Player *player, int maxInv)
{

  if (!player || maxInv < 1)
  {
    return ERROR;
  }

  inventory_set_maxObj(player->backPack, maxInv);

  return OK;
}

STATUS player_set_name(Player *player, char *name)
{

  if (!player || !name)
  {
    return ERROR;
  }

  if (!strcpy(player->name, name))
  {
    return ERROR;
  }

  return OK;
}

STATUS player_set_location(Player *player, Id location)
{

  if (!player || location == NO_ID)
  {
    return ERROR;
  }

  player->location = location;

  return OK;
}

STATUS player_set_object(Player *player, Id object)
{
  if (!player)
  {
    return ERROR;
  }

  if (ERROR == inventory_add_object(player->backPack, object))
  {
    return ERROR;
  }
  player->last_object_id = object;

  return OK;
}

STATUS player_del_object(Player *player, Id object)
{
  if (!player)
  {
    return ERROR;
  }

  if (ERROR == inventory_remove_object(player->backPack, object))
  {
    return ERROR;
  }

  player->last_object_id = object;

  return OK;
}

BackPack *player_get_backpack(Player *player)
{

  if (!player)
    return NULL;

  return player->backPack;
}

BOOL player_has_object(Player *player, Id idObject)
{
  if (!player || !idObject)
  {
    return FALSE;
  }
  return inventory_have_id(player->backPack, idObject);
}

BOOL player_can_take_an_object(Player *player, Object *object)
{

  Id id_dependency = NO_ID;

  if (!player || !object || (object_get_mobility(object) == FALSE)) /* Si los argumentos son errores o si el objeto no se puede mover, es decir, está estático en el space */
  {
    return FALSE;
  }

  /* Comprobamos las dependencias del objeto a coger */
  id_dependency = object_get_dependecy(object);

  if (id_dependency == NO_ID) /* Si no tiene dependencias, se puede coger sin problemas */
  {
    return TRUE;
  }
  else
  {
    if (player_has_object(player, id_dependency)) /* Si el objeto a coger tiene dependencia de otro, entonces se mira si el jugador porta dicho objeto del que depende */
    {
      /* Y en caso de portarlo, entonces el jugador podrá coger el objeto */
      return TRUE;
    }
  }

  return FALSE;
}

STATUS player_print(Player *player)
{

  if (!player)
  {
    return ERROR;
  }

  fprintf(stdout, "--> Player (Id: %ld; Name: %s; Location: %ld\n", player->id, player->name, player->location);
  inventory_print(player->backPack);

  return OK;
}