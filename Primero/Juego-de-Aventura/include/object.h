/**
 * @brief Administrar objetos.
 *
 * @file object.h
 * @author Alejandro Jerez
 * @version 1.0
 * @date 12-02-2021
 * @copyright GNU Public License
 */

#ifndef OBJECT_H
#define OBJECT_H

#include "space.h"
#include "types.h"
#include "link.h"

typedef struct _Object Object; /*!< Estructura de los objetos */
#define MAX_OBJECTS 20         /*!< Cantidad máxima de objetos */

/**
* @brief Crea un objet con el id recibido.
*
* @param id del objeto a crear
* @return Retorna NULL en caso de fallo o un nuevo objeto en caso de éxito.
*
* @author Alejandro Jerez
*/
Object *object_create(Id id);

/**
* @brief Destruye el objeto recibido.
*
* @param object puntero al objeto
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS object_destroy(Object *object);

/**
* @brief Obtiene el id de un objeto recibido.
*
* @param object puntero al objeto
* @return Retorna NULL en caso de fallo o el id del objeto en caso de éxito.
*
* @author Alejandro Jerez
*/
Id object_get_id(Object *object);

/**
* @brief Obtiene el nombre de un objeto recibido.
*
* @param object puntero al objeto
* @return Retorna NULL en caso de fallo o el nombre del objeto en caso de éxito.
*
* @author Alejandro Jerez
*/
const char *object_get_name(Object *object);

/**
* @brief Obtiene la descripción de un objeto recibido.
*
* @author José Miguel Nicolás García
* @param object
*
* @return NULL en caso de fallo o la descripcion del objeto en caso de éxito.
*/
const char *object_get_description(Object *object);

/**
* @brief Modifica la descripción de un objeto recibido.
*
* @author José Miguel Nicolás García
* @param object
* @param description
* @return NULL en caso de fallo u Okay si se ha realizado con éxito.
*/
STATUS object_set_description(Object *object, char *description);

/**
* @brief Cambia el nombre de un objeto por el nombre recibido.
*
* @param object puntero al objeto
* @param name nuevo nombre
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS object_set_name(Object *object, char *name);

/**
* @brief Imprime en pantalla los datos del onjeto recibido.
*
* @param object puntero al objeto
* @return Retorna ERROR si no hay objeto o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS object_print(Object *object);

/**
* @brief Comprueba si puede puede abrir o no un cierto link.
*
* @param object puntero al objeto
* @param link_id id del link
* @return Retorna OK si puede abrir el link o ERROR en caso contrario.
*
* @author Iván Fernández París
*/
STATUS object_open_link(Object *object, Id link_id);

/**
* @brief Obtiene el id del link que puede abrir el objeto.
*
* @param object puntero al objeto
* @return Retorna NULL en caso de fallo o el id del link en caso de éxito.
*
* @author Iván Fernández París
*/
Id object_get_link_id(Object *object);

/**
* @brief Establece el id del link que podrá abrir el objeto.
*
* @param object puntero al objeto
* @param id_link id del link que podra abrir el objeto
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author Iván Fernández París
*/
STATUS object_set_link_id(Object *object, Id id_link);

/**
* @brief Establece si el objeto puede o no iluminar.
*
* @param object puntero al objeto
* @param iluminate booleano que indica si podrá iluminar el objeto
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author Iván Fernández París
*/
STATUS object_set_iluminate(Object *object, BOOL iluminate);

/**
* @brief Obtiene si el objeto puede o no iluminar.
*
* @param object puntero al objeto
*
* @return Retorna FALSE si no puede iluminar o TRUE en caso de que si pueda iluminar.
*
* @author Iván Fernández París
*/
BOOL object_get_iluminate(Object *object);

/**
* @brief Obtiene si el objeto está o no iluminado.
*
* @param object puntero al objeto
*
* @return Retorna FALSE si no está iluminado o TRUE en caso de que si.
*
* @author Iván Fernández París
*/
BOOL object_is_turnedon(Object *object);

/**
* @brief Cambia el estado de la iluminación del objeto, es decir, 
* cambia de encendido a apagado y viceversa, en caso de que el objeto
* pueda iluminarse.
*
* @param object puntero al objeto
* @param turnedon booleano que indica si se apagará o se encenderá el objeto
*
* @return Retorna OK en caso de cambiarse el estado o ERROR en caso contrario
*
* @author Iván Fernández París
*/
STATUS object_set_turnedon(Object *object, BOOL turnedon);

/**
* @brief Obtiene la movilidad de un objeto.
*
* @param object puntero al objeto
* @return Retorna FALSE en caso de fallo o en caso de que el objeto no se pueda mover; o TRUE en caso de que si se pueda mover.
*
* @author Alejandro Jerez
*/
BOOL object_get_mobility(Object *object);

/**
* @brief Cambia la movilidad de un objeto por la recibida.
*
* @param object puntero al objeto
* @param movility nueva movilidad del objeto
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS object_set_movility(Object *object, BOOL movility);

/**
* @brief Obtiene el id del objeto del que el objeto indicado por parametro depende.
*
* @param object puntero al objeto
* @return Retorna NO_ID en caso de fallo o el id del objeto en caso de éxito.
*
* @author Alejandro Jerez
*/
Id object_get_dependecy(Object *object);

/**
* @brief Cambia la dependencia por la nueva indicada
*
* @param object puntero al objeto
* @param depends id del nuevo objeto del que depende
* @return Retorna ERROR en caso de fallo o OK en caso de éxito.
*
* @author Alejandro Jerez
*/
STATUS object_set_dependecy(Object *object, Id depends);

#endif
