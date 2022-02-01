/** 
 * @brief It defines a space
 * 
 * @file space.h
 * @author Profesores PPROG
 * @version 1.0 
 * @date 13-01-2015
 * @copyright GNU Public License
 */

#ifndef SPACE_H
#define SPACE_H

#include "types.h"
#include "set.h"
#include "link.h"

typedef struct _Space Space; /*!< Estructura de los espacios/casillas */

#define MAX_SPACES 100   /*!< Número máximo de espacios */
#define FIRST_SPACE 1    /*!< Espacio número uno */
#define NUM_GDESC 4      /*!< Número de filas de gdesc */
#define NUM_CHARACTERS 8 /*!< Número de columnas de gdesc */

/**
* @brief Crea un espacio con el id recibido.
* @param id id del espacio a crear
* @return retorna NULL en caso de fallo o un nuevo espacio en caso de éxito.
*
* @author Profesores PPROG
*/
Space *space_create(Id id);

/**
* @brief Destruye el espacio recibido.
* @param space espacio que se quiere destruir
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Profesores PPROG
*/
STATUS space_destroy(Space *space);

/**
* @brief Obtiene el id de un espacio recibido.
* @param space espacio del cual se quiere obtener el id
* @return retorna NULL en caso de fallo o el id del objeto en caso de éxito.
*
* @author Profesores PPROG
*/
Id space_get_id(Space *space);

/**
* @brief Cambia el nombre de un espacio por el nombre recibido.
* @param space espacio del cual se quiere obtener el establecer el nombre
* @param name nombre del espacio que se quiere establecer
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Profesores PPROG
*/
STATUS space_set_name(Space *space, char *name);

/**
* @brief Modifica la descripción de un espacio recibido. 
*
* @param space espacio del que se quiere establecer la descripcion
* @param description descripcion del espacio
* @return NULL en caso de fallo u Okay si se ha realizado con éxito.
*
* @author José Miguel Nicolás García
*/
STATUS space_set_description(Space *space, char *description);

/**
* @brief Modifica la descripción larga de un espacio recibido. 
*
* @param space espacio del que se quiere establecer la descripcion larga
* @param description descripcion larga del espacio
* @return NULL en caso de fallo u Okay si se ha realizado con éxito.
*
* @author José Miguel Nicolás García
*/
STATUS space_set_longDescription(Space *space, char *description);

/**
* @brief Obtiene el nombre de un espacio recibido.
* @param space espacio del cual se quiere obtener el nombre
* @return retorna NULL en caso de fallo o el nombre del espacio en caso de éxito.
*
* @author Profesores PPROG
*/
const char *space_get_name(Space *space);

/**
* @brief Obtiene la descripción de un espacio recibido. 
* @param space espacio del que se quiere obtener la descripcion
* @return NULL en caso de fallo o la descripcion del espacio en caso de éxito.
*
* @author José Miguel Nicolás García
*/
const char *space_get_description(Space *space);

/**
* @brief Obtiene la descripción larga de un espacio recibido. 
* @param space espacio del que se quiere obtener la descripcion larga
* @return NULL en caso de fallo o la descripcion del espacio en caso de éxito.
*
* @author José Miguel Nicolás García
*/
const char *space_get_longDescription(Space *space);
/**
* @brief Cambia el id del link del norte de un espacio por el id del link recibido por parametros.
* @param space espacio del cual se quiere establecer el enlace al norte
* @param id Id del enlace que representa la conexion que establecera con el norte
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS space_set_north(Space *space, Id id);

/**
* @brief Obtiene link_id del norte de un espacio recibido.
* @param space espacio del cual se quiere obtener el enlace al norte
* @return retorna NULL en caso de fallo o el id del espacio al norte caso de éxito.
*
* @author Javier Fraile Iglesias
*/
Id space_get_north(Space *space);

/**
* @brief Cambia el id del link del sur de un espacio por el id del link recibido por parametros.
* @param space espacio del cual se quiere establecer el enlace al sur
* @param id Id del enlace que representa la conexion que establecera con el sur
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS space_set_south(Space *space, Id id);

/**
* @brief Obtiene link_id del sur de un espacio recibido.
* @param space espacio del cual se quiere obtener el enlace al sur
* @return retorna NULL en caso de fallo o el id del espacio al sur caso de éxito.
*
* @author Javier Fraile Iglesias
*/
Id space_get_south(Space *space);

/**
* @brief Cambia el id del link del este de un espacio por el id del link recibido por parametros.
* @param space espacio del cual se quiere establecer el enlace al este
* @param id Id del enlace que representa la conexion que establecera con el este
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS space_set_east(Space *space, Id id);

/**
* @brief Obtiene id del link_id del este de un espacio recibido.
* @param space espacio del cual se quiere obtener el enlace al este
* @return retorna NULL en caso de fallo o el id del espacio al sur caso de éxito.
*
* @author Javier Fraile Iglesias
*/
Id space_get_east(Space *space);

/**
* @brief Cambia el id del link del oeste de un espacio por el id del link recibido por parametros.
* @param space espacio del cual se quiere establecer el enlace al oeste
* @param id Id del enlace que representa la conexion que establecera con el oeste
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS space_set_west(Space *space, Id id);

/**
* @brief Obtiene link_id del oeste de un espacio recibido.
* @param space espacio del cual se quiere obtener el enlace al oeste
* @return retorna NULL en caso de fallo o el id del espacio al sur caso de éxito.
*
* @author Javier Fraile Iglesias
*/
Id space_get_west(Space *space);

/**
* @brief Cambia el id del link de arriba de un espacio por el id del link recibido por parametros.
* @param space espacio del cual se quiere establecer el enlace al espacio de arriba
* @param id Id del enlace que representa la conexion que establecera con el espacio de arriba
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS space_set_up(Space *space, Id id);

/**
* @brief Obtiene link_id de arriba de un espacio recibido.
* @param space espacio del cual se quiere obtener el enlace de arriba
* @return retorna NULL en caso de fallo o el id del espacio de arriba en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
Id space_get_up(Space *space);

/**
* @brief Cambia el id del link de abajo de un espacio por el id del link recibido por parametros.
* @param space espacio del cual se quiere establecer el enlace al espacio de abajo
* @param id Id del enlace que representa la conexion que establecera con el espacio de abajo
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS space_set_down(Space *space, Id id);

/**
* @brief Obtiene link_id de abajo de un espacio recibido.
* @param space espacio del cual se quiere obtener el enlace de abajo
* @return retorna NULL en caso de fallo o el id del espacio de abajo en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
Id space_get_down(Space *space);

/**
* @brief Cambia el object de un espacio por el id del espacio recibido.
* @param space espacio del cual se quiere establecer el objeto
* @param id Id del del objeto que se quiere establecer
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejando Jerez
*/
STATUS space_set_object(Space *space, Id id);

/**
* @brief Obtiene el id de un objeto si hay un objeto en el espacio recibido.
* @param space espacio del cual se quiere obtener los objetos
* @return retorna NO_ID en caso de error o el id del objeto del espacio en caso de éxito.
*
* @author José Miguel Nicolás García
*/
Id *space_get_objects(Space *space);

/**
* @brief Comprueba si el objeto se ecuentra en el espacio.
* @param space espacio del cual se quiere comprobar el objeto
* @param objId id del objeto que que quiere buscar en dicho espacio
* @return retorna FALSE en caso de error o de que no esté o TRUE en caso de éxito(el objeto está en el espacio).
*
* @author Alejandro Jerez Reinoso
*/
BOOL space_object_presence(Space *space, Id objId);

/**
* @brief Elimina un objeto del espacio.
* @param space espacio del cual se quiere eliminar el objeto
* @param id id del objeto que que quiere eliminar
* Retorna ERROR en caso de error o OK en caso de éxito.
*
* @author Alejandro Jerez Reinoso
*/
STATUS space_del_object(Space *space, Id id);

/**
 * @brief Obtiene el numero de objetos que contiene un espacio
 * @param  space espacio del que se quiere comprobar el numero de objetos
 * @return returna el numero de objetos en caso correcto, o NO_ID en caso contrario
 * 
 * @author Javier Fraile Iglesias
 */
int space_get_num_objects(Space *space);

/**
 * @brief Comprueba si el espacio contiene un determinado objeto
 * 
 * @author José Miguel Nicolás García
 * @date 09-04-2021
 * 
 * @param  space puntero al espacio
 * @param  object del espacio
 * @return True en caso de tenerlo y False en caso contrario
 */
BOOL space_have_id(Space *space, Id object);

/**
* @brief Añade una línea a de caracteres (dibujos) al espacio
* @param space espacio del cual se quiere establecer una linea de la casilla
* @param line linea (de la casilla )que se quiere insertar a dicho espacio
* @param pos numero de fila que representa la linea de dicha casilla
* @return retorna ERROR en caso de error o OK en caso de éxito.
*
* @author Alejandro Jerez Reinoso
*/

STATUS space_set_gdesc(Space *space, char *line, int pos);

/**
* @brief Obtiene una determinada línea de caractreres contenida en el espacio
* @param space espacio del cual se quiere obtener una linea de la casilla
* @param pos numero de fila que representa la linea de dicha casilla
* @return retorna NULL en caso de error o la línea de caracteres en caso de éxito.
*
* @author Alejandro Jerez Reinoso
*/
const char *space_get_gdesc(Space *space, int pos);

/**
* @brief Establece si la casilla del espacio esta iluminada o no
* @param space espacio del cual se quiere obtener si esta ilumninada
* @param iluminado estado al que se quiere cambiar esa casilla
* @return retorna ERROR en caso de error o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/

STATUS space_set_iluminado(Space *space, BOOL iluminado);

/**
* @brief Obtiene el estado de iluminacion de la casilla del espacio
* @param space espacio del cual se quiere obtener la iluminacion
* @return retorna FALSE en caso de error o de estado apagado, y TRUE en caso de estado iluminado
*
* @author Javier Fraile Iglesias
*/
BOOL space_get_iluminado(Space *space);

/**
* @brief Imprime en pantalla los datos del espacio recibido.
* @param space espacio el cual se quiere imprimir el contenido
* @return retorna ERROR si no existe el espacio u OK en caso de éxito.
*
* @author Profesores PPROG
*/
STATUS space_print(Space *space);

#endif
