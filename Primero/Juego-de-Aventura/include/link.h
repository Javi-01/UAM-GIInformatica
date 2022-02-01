/** 
 * @brief It defines a Link
 * 
 * @file link.h
 * @author Javier Fraile Iglesias
 * @version 1.0 
 * @date 13-01-2015
 * @copyright GNU Public License
 */

#ifndef LINK_H
#define LINK_H

#include "types.h"

typedef struct _Link Link; /*!< Estructura de los enlaces */

#define MAX_LINKS 100 /*!< Número máximo de enlaces */

/**
* @brief Crea un enlace con el id recibido.
* @param id id del link que se quiere crear 
* @return retorna NULL en caso de fallo o un nuevo enlace en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
Link *link_create(Id id);

/**
* @brief Destruye el enlace recibido.
* @param link id del link que se quiere destruir
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS link_destroy(Link *link);

/**
* @brief Obtiene el id de un enlace recibido.
* @param link link del cual se quiere obtener el id
* @return retorna NULL en caso de fallo o el id del objeto en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
Id link_get_id(Link *link);

/**
* @brief Cambia el nombre del enlace por el nombre recibido.
* @param link link del cual se quiere establecer el nombre
* @param name nombre que se quiere establecer al enlace
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS link_set_name(Link *link, char *name);

/**
* @brief Obtiene el nombre de un enlace recibido.
* @param link link del cual se quiere obtener el nombre
* @return retorna NULL en caso de fallo o el nombre del enlace en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
const char *link_get_name(Link *link);

/**
* @brief Cambia el id del primer enlace por el id recibido .
* @param link link del cual se quiere establecer el primer id
* @param id id que se quiere establecer como primero
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS link_set_first_id(Link *link, Id id);

/**
* @brief Obtiene el id del primer enlace recibido.
* @param link link del cual se quiere obtener el primer id
* @return retorna NULL en caso de fallo o el id del enlace al norte caso de éxito.
*
* @author Javier Fraile Iglesias
*/
Id link_get_first_id(Link *link);

/**
* @brief Cambia el id del segundo enlace por el id recibido .
* @param Link link del cual se quiere establecer el segundo id
* @param id id que se quiere establecer como segundo
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS link_set_second_id(Link *Link, Id id);

/**
* @brief Obtiene el id del segundo id de un enlace recibido.
* @param Link link del cual se quiere obtener el segundo id
* @return retorna NULL en caso de fallo o el id del enlace al sur caso de éxito.
*
* @author Javier Fraile Iglesias
*/
Id link_get_second_id(Link *Link);

/**
* @brief Obtiene el id del link que se corresponde con la pareja del mismo
* @param link link del cual se quiere obtener la pareja
* @param id id del que se quiere buscar la pareja
* @return retorna el id en caso de que sea valido o NO_ID en caso de no encontrarlo
*
* @author Javier Fraile Iglesias
*/
Id link_get_pair_id(Link *link, Id id);
/**
* @brief Cambia el estado del enlace por el pasado en el status.
* @param Link link del cual se quiere establecer el estado del enlace
* @param status estado del enlace (Abierto = ABIERTO, Cerrado = CERRADO)
* @return retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS link_set_status(Link *Link, int status);

/**
* @brief Obtiene el estado del enlace .
* @param link link del cual se quiere obtener el estado del enlace
* @return retorna -1 en caso de fallo o el estado del enlace al este caso de éxito.
*
* @author Javier Fraile Iglesias
*/
int link_get_status(Link *link);

/**
* @brief Imprime en pantalla los datos del enlace recibido.
* @param link link el cual se quiere imprimir
* @return retorna ERROR si no existe el enlace u OK en caso de éxito.
*
* @author Javier Fraile Iglesias
*/
STATUS link_print(Link *link);

#endif
