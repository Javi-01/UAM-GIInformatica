/**
 * @brief It defines a textual graphic engine
 *
 * @file graphic_engine.h
 * @author Profesores PPROG
 * @version 2.0
 * @date 07-02-2021
 * @copyright GNU Public License
 */

#ifndef __GRAPHIC_ENGINE__
#define __GRAPHIC_ENGINE__

#include "game.h"

typedef struct _Graphic_engine Graphic_engine; /*!< Estrucutura graphic engine */

/**
* @brief Inicia todo el motor gráfico (declara todos los componentes de la estructura Graphic_engine e inicializa la salida 
* por pantalla).
* @return Devuelve un puntero al Graphic_engine creado. 
*
* @author Profesores PPROG
*/
Graphic_engine *graphic_engine_create();

/**
* @brief Destruye todos los componentes del tipo de dato Graphic_engine, destruye la salida por pantalla creada y libera la memoria.
* @param ge puntero a graphic_engine
*
* @author Profesores PPROG
*/
void graphic_engine_destroy(Graphic_engine *ge);

/**
* @brief Dibuja toda la interfaz gráfica mostrada en la pantalla.
* @param ge puntero a graphic_engine
* @param game puntero a game
*
* @author Modificado por Iván Fernández París
*/
void graphic_engine_paint_game(Graphic_engine *ge, Game *game);

/**
* @brief NO aparece en el.c
* @param ge puntero a graphic_engine
* @param str cadena 
*
* @author Profesores PPROG
*/
void graphic_engine_write_command(Graphic_engine *ge, char *str);

#endif
