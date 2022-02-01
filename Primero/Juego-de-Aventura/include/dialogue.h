/**
 * @brief It defines the header of set
 *
 * @file set.h
 * @version 2.0
 * @date 06-05-2021
 * @copyright GNU Public License
 */

#ifndef DIALOGUE_H
#define DIALOGUE_H
#include "types.h"
#include "game.h"
#include "command.h"

typedef struct _Dialogue Dialogue; /*!< Estructura de los diálogos */

/**
* @brief Crea dialogo.
*
* @return Retorna NULL en caso de fallo o un nuevo dialogo en caso de éxito.
*
* @author José Miguel Nicolás
*/
Dialogue *dialogue_create();

/**
* @brief Destruye diálogo.
*
* @param dialogue puntero al diálogo
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author José Miguel Nicolás
*/
STATUS dialogue_destroy(Dialogue *dialogue);

/**
* @brief Cambia el último comando
*
* @param dialogue puntero al diálogo
* @param command ultimo comando del juego
* @param status ultimo status del juego
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author José Miguel Nicolás
*/
STATUS dialogue_set_command(Dialogue *dialogue, char *command, char *status);

/**
* @brief Obtiene la frase correspondiente al último comando ejecutado
*
* @param game puntero a game
* @param command último comando del juego
* @param status último status del juego
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author José Miguel Nicolás
*/
char *dialogue_get_explicative_phrase(void *game, char *command, char *status);
#endif
