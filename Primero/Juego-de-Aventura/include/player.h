/**
 * @brief Implementación de la estructura de objetos y sus funciones.
 *
 * @file player.h
 * @author Alejandro Jerez 
 * @version 1.0
 * @date 18-02-2021
 * @copyright GNU Public License
 */

#ifndef PLAYER_H
#define PLAYER_H

#include "types.h"
#include "space.h"
#include "inventory.h"
#include "object.h"

typedef struct _Player Player; /*!< Estructura de los jugadores */

/**
* @brief Crea un jugador con el id recibido. 
*
* @param id del nuevo jugador
* @return Retorna NULL en caso de fallo o un nuevo jugador en caso de éxito.
*
* @author Alejandro Jerez
*/
Player *player_create(Id id);

/**
* @brief Destruye el jugador recibido.
*
* @param player puntero al jugador
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS player_destroy(Player *player);

/**
* @brief Obtiene el id de un jugador recibido.
*
* @param player puntero al jugador
* @return Retorna NULL en caso de fallo o el id del jugador en caso de éxito.
*
* @author Alejandro Jerez
*/
Id player_get_id(Player *player);

/**
* @brief Obtiene el nombre de un jugador recibido.
*
* @param player puntero al jugador
* @return Retorna NULL en caso de fallo o el nombre del jugador en caso de exito.
*
* @author Alejandro Jerez
*/
const char *player_get_name(Player *player);

/**
* @brief Obtiene el id de la localización de un jugador recibido.
*
* @param player puntero al jugador
* @return Retorna NULL en caso de fallo o el id de la localización en caso de éxito.
*
* @author Alejandro Jerez
*/
Id player_get_location(Player *player);

/**
* @brief Obtiene los ids de los objetos de un jugador recibido.
*
* @param player puntero al jugador
* @return  Retorna NULL en caso de fallo o un array de ids de los objetos en caso de éxito.
*
* @author Alejandro Jerez
*/
Id *player_get_objects(Player *player);

/**
* @brief Cambia el nombre de un jugador por el nombre recibido.
*
* @param player puntero al jugador
* @param name nuevo nombre del jugador
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS player_set_name(Player *player, char *name);

/**
* @brief Cambia el id de la localización de un jugador por el id recibido.
*
* @param player puntero al jugador
* @param location id del nuevo espacio del jugador
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS player_set_location(Player *player, Id location);

/**
* @brief Cambia el id del objeto de un jugador por el id recibido.
*
* @param player puntero al jugador
* @param object nuevo objeto del jugador
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS player_set_object(Player *player, Id object);

/**
* @brief Obtiene el número de objetos que porta el jugador recibido.
*
* @param player puntero al jugador
* @return Retorna -1 en caso de ERROR o el número de objetos en caso de éxito.
*
* @author Alejandro Jerez
*/
int player_get_num_of_objects(Player *player);

/**
* @brief Cambia la cantidad máxima del inventario del jugador.
*
* @param player puntero al jugador
* @param maxInv tamaño máximo de la mochila del jugador
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS player_set_max_inv(Player *player, int maxInv);

/**
* @brief Comprueba si el jugador tiene el onjeto recibido como argumento
*
* @param player puntero al jugador
* @param idObject id del objeto a comprobar
* @return Retorna TRUE en caso de que lo tenga o FALSE en el caso contrario.
*
* @author Alejandro Jerez
*/
BOOL player_has_object(Player *player, Id idObject);

/**
* @brief HABRÁ QUE BORRARLA

* @author Alejandro Jerez
*/
Id player_get_object(Player *player);

/**
* @brief Imprime en pantalla los datos del jugador recibido.
*
* @param player puntero al jugador
* @return Retorna ERROR si no hay objeto o OK en caso de éxito.
* 
* @author Alejandro Jerez
*/
STATUS player_print(Player *player);

/**
* @brief Devuelve la mochila que porta el jugador
*
* @param player puntero al jugador
* @return Retorna NULL en caso de error o la mochila del jugador en caso de éxito.
*
* @author Iván Fernández París
*/
BackPack *player_get_backpack(Player *player);

/**
* @brief Devuelve el id del último objeto que haya sido o bien añadido o eliminado del inventario del jugador
*
* @param player puntero al jugador
* @return Retorna NO_ID si no hay objeto o el id del último objeto o bien añadido o eliminado del inventario del jugador.
*
* @author Iván Fernández París
*/
Id player_get_last_object(Player *player);

/**
* @brief Se encarga de eliminar un objeto del inventario del jugador
*
* @param player puntero al jugador
* @param object id del objeto
* @return Retorna ERROR en caso de error o OK en caso de éxito.
*
* @author Iván Fernández París
*/
STATUS player_del_object(Player *player, Id object);

/**
* @brief Se encarga de comprobar si el jugador puede coger o no un determinado objeto
* a partir de las dependencias que tenga dicho objeto
*
* @param player puntero al jugador
* @param object puntero al objeto
*
* @return Retorna FALSE en caso de poder coger el objeto o TRUE en caso de poder.
*
* @author Iván Fernández París
*/
BOOL player_can_take_an_object(Player *player, Object *object);

#endif