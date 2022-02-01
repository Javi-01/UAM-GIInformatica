/** 
 * @brief It defines the game interface
 * for each command
 * 
 * @file game.h
 * @author Profesores PPROG
 * @version 1.0 
 * @date 13-01-2015 
 * @copyright GNU Public License
 */

#ifndef GAME_H
#define GAME_H

#include "command.h"
#include "space.h"
#include "player.h"
#include "object.h"
#include "die.h"
#include "set.h"
#include "link.h"
#include "inventory.h"
#include "dialogue.h"

#define DEAD_SPACE_1 7    /*!< Indica la casilla de la muerte por agujero */
#define DEAD_SPACE_2 12   /*!< Indica la casilla de la muerte por agujero */
#define WIN_SPACE 10      /*!< Indica la casilla de finalizacion */
#define NUM_WIN_OBJECTS 7 /*!< Numero de objetos para ganar */

typedef struct _Game Game; /*!< Estrucutura game */

/**
* @brief Crea un juego (instancia cada componente)
* @return Devuelve OK si se ha creado el juego. 
*
* @author Modificada por Iván Fernández París
*/
Game *game_create();

/**
* @brief Crea un juego y carga espacios y localizaciones con la información de un fichero.
* @param game puntero a game 
* @param filename cadena con el nombre del fichero del cual son leidos los datos del juegos
* @return Devuelve OK si se ha creado el juego y ERROR en caso contrario.
*
* @author Modificada por Iván Fernández París
*/
STATUS game_create_from_file(Game *game, char *filename);
/**
* @brief Crea un juego y carga espacios y localizaciones con la información de un fichero.
* @param game puntero a game 
* @param cmd objeto de tipo T_Command
* @return Devuelve OK si se ha creado el juego y ERROR en caso contrario.
*
* @author Profesores PPROG
*/
STATUS game_update(Game *game, T_Command cmd);

/**
* @brief Destruye el juego y sus componentes. 
* @param game puntero a game 
* @return Devuelve OK si se ha cerrado el juego.
*
* @author Profesores PPROG
*/
STATUS game_destroy(Game *game);

/**
* @brief Destruye el objeto en una posicion de la estructura concreta 
* @param game puntero a game 
* @param pos posicion del objeto
* @return Devuelve OK si se eliminado.
*
* @author Javier Fraile Iglesias e Iván Fenández París
*/
STATUS game_free_objects(Game *game, Id pos);

/**
* @brief Destruye el link en una posicion de la estructura concreta 
* @param game puntero a game 
* @param pos posicion del link
* @return Devuelve OK si se eliminado.
*
* @author Javier Fraile Iglesias e Iván Fenández París
*/
STATUS game_free_links(Game *game, Id pos);

/**
* @brief Destruye el space en una posicion de la estructura concreta 
* @param game puntero a game 
* @param pos posicion del space
* @return Devuelve OK si se eliminado.
*
* @author Javier Fraile Iglesias e Iván Fenández París
*/
STATUS game_free_spaces(Game *game, Id pos);

/**
* @brief Destruye el jugar del juego. 
* @param game puntero a game 
* @return Devuelve OK si se eliminado.
*
* @author Javier Fraile Iglesias e Iván Fenández París
*/
STATUS game_free_player(Game *game);
/**
* @brief Comprueba si el juego ha acabado.
* @param game puntero a game 
* @return Devuelve FALSE
*
* @author Profesores PPROG
*/
BOOL game_is_over(Game *game);

/**
* @brief Muestra todos los datos por pantalla.
* @param game puntero a game 
*
* @author Profesores PPROG
*/
void game_print_data(Game *game);

/**
* @brief Devuelve el espacio que tenga el ID introducido.
* @param game puntero a game 
* @param id Id del espacio el cual se quiere coger  
* @return Devuelve un Space
*
* @author Profesores PPROG
*/
Space *game_get_space(Game *game, Id id);

/**
* @brief Devuelve el jugador registrado en el juego.
* @param game puntero a game 
* @return Devuelve un Player
*
* @author Iván Fernández París
*/
Player *game_get_player(Game *game);

/**
* @brief Devuelve el valor del último lanzamiento del dado.
* @param game puntero a game 
* @return valor del último lanzamiento del dado
*
* @author Iván Fernández París
*/
long game_get_last_roll(Game *game);

/**
* @brief Llama a la función player_get_location.
* @param game puntero a game 
* @return Devuelve un ID.
*
* @author Profesores PPROG
*/
Id game_get_player_location(Game *game);

/**
* @brief Aporta el ID de la localizacion del objeto.
* @param game puntero a game 
* @param id id del objeto el cual será buscado 
* @return Devuelve un ID.
*
* @author Profesores PPROG
*/
Id game_get_object_location(Game *game, Id id);

/**
* @brief Devuelve el diálogo de game.
* @param game puntero a game 
* @return El diálogo
*
* @author José Miguel Nicolás García
*/
Dialogue *game_get_dialogue(Game *game);

/**
* @brief Añade un espacio al juego
* @param game puntero a game 
* @param space puntero a space 
* @return Devuelve OK en caso de que funcione bien y ERROR en caso contrario.
*
* @author Profesores PPROG
*/
STATUS game_add_space(Game *game, Space *space);

/**
* @brief Devuelve id del objeto según su posición en el array.
* @param game puntero a game 
* @param posicion posición del objeto en el array de objetos de game
* @return Devuelve id Objeto
*
* @author José Miguel Nicolás García
*/
Id game_get_object_id(Game *game, int posicion);
/**
* @brief Devuelve todos los objetos.
* @param game puntero a game 
* @return Devuelve un puntero al array de objetos o null si se ha producido un error.
*
* @author José Miguel Nicolás García
*/
Object *game_get_objects(Game *game);
/**
* @brief Añade un objeto al juego.
* @param game puntero a game 
* @param object puntero a objeto 
* @return Devuelve OK en caso de que funcione bien y ERROR en caso contrario.
*
* @author José Miguel Nicolás García
*/
STATUS game_add_object(Game *game, Object *object);
/**
* @brief Añade un jugador al juego.
* @param game puntero a game 
* @param player puntero a player
* @return Devuelve OK en caso de que funcione bien y ERROR en caso contrario.
*
* @author Iván Fenández París
*/
STATUS game_add_player(Game *game, Player *player);

/**
* @brief Devuelve el ultimo comando de la consola
* @param game puntero a game 
* @return Devuelve game->last_cmd
* 
* @author Profesores PPROG
*/
T_Command game_get_last_command(Game *game);

/**
* @brief Devuelve el ultimo estado de un comando de la consola
* @param game puntero a game 
* @return Devuelve game->last_cmd_status
*
* @author Profesores PPROG
*/
STATUS game_get_last_cmd_status(Game *game);

/**
* @brief Devuelve el objecto registrado en game que tiene dicho id recibido por argumentos
* @param game puntero a game 
* @param id Id del objeto
* @return Devuelve game->object[i]
*
* @author Iván Fernandez París
*/
Object *game_get_object_at_id(Game *game, Id id);

/**
* @brief Devuelve el objecto registrado en game que tiene dicho nombre recibido por argumentos
* @param game puntero a game 
* @param object_name nombre del objeto a buscar 
* @return Devuelve game->object[i]
*
* @author Iván Fernandez París
*/
Object *game_get_object_at_name(Game *game, char *object_name);

/**
* @brief Devuelve el link cuya id corresponde con el que recibe por parámetros
* @param game puntero a game 
* @param id Id del link
* @return Link cuyo Id es el id recibido por parámetros
*
* @author Iván Fernandez París
*/
Link *game_get_link_at_id(Game *game, Id id);

/**
* @brief Devuelve el link cuyo nombre corresponde con el que recibe por parámetros
* @param game puntero a game 
* @param link_name nombre del link
* @return Link cuyo nombre es el id recibido por parámetros
*
* @author Iván Fernandez París
*/
Link *game_get_link_at_name(Game *game, char *link_name);

/**
* @brief Añade un link al juego.
* @param game puntero a game 
* @param link puntero a link
* @return Devuelve OK en caso de que funcione bien y ERROR en caso contrario
*
* @author Iván Fernandez París
*/
STATUS game_add_link(Game *game, Link *link);

/**
* @brief Devuelve un objeto a partir de la posición del array que recibe por parámetros.
* @param game puntero a game 
* @param position posicion del objeto 
* @return Devuelve un objeto de tipo Object en caso de que se encuentre un objeto en la posición recibida o NULL en caso contrario
*
* @author Iván Fernandez París
*/
Object *game_get_object_at_position(Game *game, int position);

/**
* @brief Devuelve el ultimo tipo de movimiento realizado
* @param game puntero a game 
* @return Devuelve game->last_move
*
* @author Iván Fernández París
*/
const char *game_get_last_move(Game *game);

/**
* @brief Devuelve la última descripción llamada.
* @param game puntero a game 
* @return Devuelve un puntero al string o null si se ha producido un error
* 
* @author José Miguel Nicolás García
*/
char *game_get_lastDescription(Game *game);

/**
* @brief Comprueba si el juego ha finalizado por muerte o por vicotia 
* @param game puntero a game 
* 
* @author Iván Fernández París
*/
void game_manage_final(Game *game);

/**
* @brief Comprobar el estado del juego 
* @param game puntero a game
* @return devuelve el estado del juego 
* 
* @author Iván Fernández París
*/
BOOL game_get_finished(Game *game);

#endif
