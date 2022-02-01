#ifndef SET_H
#define SET_H
#include "types.h"
#define NUM_IDs 10       /*!< Numero de identificadores */
typedef struct _Set Set; /*!< Estructura de set */

/**
* @brief Crea un nuevo conjunto
* @return Conjunto creado o NULL en caso de error.
* @author José Miguel Nicolás García
*/
Set *set_create();

/**
* @brief Elimina un conjunto.
* @param set puntero al conjunto.
* @return  ERROR en caso de fallo u OK si se ha eliminado correctamente.
* @author José Miguel Nicolás García
*/
STATUS set_destroy(Set *set);

/**
* @brief Añade un elemento al conjunto.
* @param set puntero al conjunto.
* @param newId Id del nuevo elemento
* @return  ERROR en caso de fallo u OK si se ha añadido correctamente.
* @author José Miguel Nicolás García
*/
STATUS set_add(Set *set, Id newId);

/**
* @brief Elimina un elemento del conjunto.
* @param set puntero al conjunto.
* @param id Id del elemento a eliminar.
* @return  ERROR en caso de fallo u OK si se ha eliminado correctamente.
* @author José Miguel Nicolás García
*/
STATUS set_del(Set *set, Id id);

/**
* @brief Comprueba si hay un elemento en el conjunto.
* @return  FALSE en caso de no encontrarlo o TRUE en caso contrario.
* @author José Miguel Nicolás García
*/
BOOL set_have_id(Set *set, Id id);

/**
* @brief Muestra por pantalla un conjunto
* @param set puntero al conjunto.
* @return  ERROR en caso de fallo u OK en caso contrario.
* @author José Miguel Nicolás García
*/
STATUS set_print(Set *set);

/**
* @brief Devuelve el id de una posicion
* @param set puntero al conjunto.
* @param pos número de posición.
* @return  id.
* @author José Miguel Nicolás García
*/
Id set_get_id(Set *set, int pos);

/**
* @brief Devuelve el id de la ultima posicion
* @param set puntero al conjunto.
* @author José Miguel Nicolás García
*/
Id set_get_last_id(Set *set);
/**
* @brief Devuelve la cantidad actual del conjunto
* @param set puntero al conjunto.
* @return cantidad.
* @author José Miguel Nicolás García
*/
int set_get_cantidad(Set *set);

/**
* @brief Devuelve el conjunto de ids que tiene el set
* @return ids
* @author Iván Fernández París
*/
Id *set_get_ids(Set *set);
#endif