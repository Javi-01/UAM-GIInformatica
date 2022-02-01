/** 
 * @brief It implements the game spaces lecture
 * 
 * @file gameManagement.c
 * @author Modificado Javier Fraile Iglesias e Iván Fernández París
 * @version 1.0 
 * @date 11-02-2021 
 * @copyright GNU Public License
 */

#ifndef GAMEMANAGEMENT_H
#define GAMEMANAGEMENT_H

#include "command.h"
#include "space.h"
#include "game.h"
#include "link.h"

#define TOP_LINE 0 /* !< Linea superior del gdesc cogida del data.dat */
#define MID_LINE 1 /* !< Linea del medio del gdesc cogida del data.dat */
#define BOT_LINE 2 /* !< Linea inferior del gdesc cogida del data.dat */

/**
* @brief Añade todas las casillas (y sus conexiones) desde el fichero.
*
* @param game puntero al juego
* @param filename nombre del fichero con los datos del juego
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Modificado por Javier Fraile Iglesias e Iván Fernández París
*/
STATUS game_management_spaces(Game *game, char *filename);

/**
* @brief Añade todos los objetos desde el fichero.
*
* @param game puntero al juego
* @param filename nombre del fichero con los datos del juego
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author José Miguel Nicolás García e Iván Fernández París la parte de realizar la lectura de la descripción de un objeto, si es movible, si se enciende... 
*/

STATUS game_management_objects(Game *game, char *filename);

/**
* @brief Añade al jugador desde el fichero.
*
* @param game puntero al juego
* @param filename nombre del fichero con los datos del juego
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez Reinoso
*/
STATUS game_management_player(Game *game, char *filename);

/**
* @brief Añade todos los enlaces y su estado desde el fichero.
*
* @param game puntero al juego
* @param filename nombre del fichero con los datos del juego
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS game_management_links(Game *game, char *filename);

/**
* @brief Funcion encargada de guardar la partida de un fichero en el estado en el 
*       que se quedó
*
* @param game puntero al juego
* @param filename nombre del fichero con los datos del juego
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias e Iván Fernández París
*/
STATUS game_management_save(Game *game, char *filename);

/**
* @brief Funcion encargada de cargar la partida de un fichero en el estado en el 
*       que se quedó
*
* @param game puntero al juego
* @param filename nombre del fichero con los datos del juego
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias e Iván Fernández París
*/
STATUS game_management_load(Game *game, char *filename);

#endif