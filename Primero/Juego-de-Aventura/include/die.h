/**
 * @brief Administrar un dado.
 *
 * @file die.h
 * @author Alejandro Jerez
 * @version 1.0
 * @date 04-03-2021
 * @copyright GNU Public License
 */
#ifndef DIE_H
#define DIE_H

#include "types.h"

typedef struct _Die Die; /*!< Estructura del dado */

/**
* @brief Crea un dado con el id, el número máximo y el mínimo recibidos. 
*
* @param id del nuevo dado
* @param max número máximo al que llegará el dado
* @param min número mínimo al que llegará el dado
* @return Retorna NULL en caso de fallo o un nuevo dado en caso de éxito.
*
* @author Alejandro Jerez
*/
Die *die_create(Id id, long max, long min);

/**
* @brief Destruye el dado recibido. 
*
* @param die puntero al dado
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS die_destroy(Die *die);

/**
* @brief Hace una tirada del dado generando un número aleatorio entre el máx y
* el min del dado recibido. 
*
* @param die puntero al dado
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS die_roll(Die *die);

/**
* @brief Obtiene el id de un dado recibido.
*
* @param die puntero al dado
* @return Retorna NO_ID en caso de fallo o el id del dado en caso de éxito.
*
* @author Alejandro Jerez
*/
Id die_get_id(Die *die);

/**
* @brief Obtiene el número máximo de un dado recibido.
*
* @param die puntero al dado
* @return Retorna -1 en caso de fallo o el número máximo del dado en caso de éxito.
*
* @author Alejandro Jerez
*/
long die_get_max(Die *die);

/**
* @brief Obtiene el número mínimo de un dado recibido.
*
* @param die puntero al dado
* @return Retorna -1 en caso de fallo o el número minimo del dado en caso de éxito.
*
* @author Alejandro Jerez
*/
long die_get_min(Die *die);

/**
* @brief Obtiene el número de la última tirada de un dado recibido.
*
* @param die puntero al dado
* @return Retorna -1 en caso de fallo o el número de la última tirada del dado en caso de éxito.
*
* @author Alejandro Jerez
*/
long die_get_num(Die *die);

/**
* @brief Cambia el número máximo de un dado recibido, por un nuevo máximo recibido.
*
* @param die puntero al dado
* @param newMax nuevo número máximo del dado
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS die_set_max(Die *die, long newMax);

/**
* @brief Cambia el número mínimo de un dado recibido, por un nuevo mínimo recibido.
*
* @param die puntero al dado
* @param newMin nuevo número mínimo del dado
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS die_set_min(Die *die, long newMin);

/**
* @brief Imprime todos los campos del dado recibido. 
*
* @param die puntero al dado
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS die_print(Die *die);

#endif
