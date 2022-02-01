/**
 * @brief It defines the header of inventory
 *
 * @file inventory.h
 * @author José Miguel Nicolás García 
 * @version 1.0
 * @date 30-03-2019
 * @copyright GNU Public License
 */
#include "types.h"
#include "set.h"
#ifndef INVENTORY_H
#define INVENTORY_H

#define UNDIFINED_SIZE -1 /*!< Tamaño no definido */

typedef struct _BackPack BackPack; /*!< Estructura de inventario */

/**
* @brief Crea un nuevo inventario.
* @return retorna NULL en caso de fallo o un nuevo inventario en caso de éxito.
*
* @author José Miguel Nicolás García
*/
BackPack *inventory_create();

/**
* @brief Elimina un inventario
* @param backPack puntero a el inventario
* @author José Miguel Nicolás García
*/
void inventory_destroy(BackPack *backPack);

/**
* @brief Añade objeto al inventario.
* @param backPack puntero a inventario
* @param newObject Id del objeto añadido.
* @return retorna ERROR en caso de fallo u Okay en caso contrario.
* @author José Miguel Nicolás García
*/
STATUS inventory_add_object(BackPack *backPack, Id newObject);

/**
* @brief Elimina objeto del inventario.
* @param backPack puntero a inventario.
* @param id id del objeto
* @return retorna ERROR en caso de fallo u Okay en caso contrario.
* @author José Miguel Nicolás García
*/

STATUS inventory_remove_object(BackPack *backPack, Id id);

/**
* @brief Obtiene objeto del inventario.
* @param backPack puntero a inventario.
* @param position número de posicion.
* @return retorna NO_ID en caso de fallo o el Id del objeto en caso contrario.
* @author José Miguel Nicolás García
*/
Id inventory_get_object(BackPack *backPack, int position);

/**
* @brief Obtiene el último objeto introducido al inventario.
* @param backPack puntero a inventario.
* @return retorna NO_ID en caso de fallo o el Id del objeto en caso contrario.
* @author José Miguel Nicolás García
*/
Id inventory_get_last_object(BackPack *backPack);

/**
* @brief Obtiene la cantidad máxima de objetos en el inventario
* @param backPack puntero a inventario.
* @return retorna -1 en caso de fallo o la cantidad.
* @author José Miguel Nicolás García
*/
int inventory_get_maxObj(BackPack *backPack);

/**
* @brief Obtiene el conjunto del inventario
* @param backPack puntero a inventario.
* @return retorna NULL en caso de fallo o el set en caso contrario.
* @author José Miguel Nicolás García
*/
Set *inventory_get_set(BackPack *backPack);

/**
* @brief Cambia el número máximo de objetos.
* @param backPack puntero a inventario.
* @param maxObj nuevo máximo de objetos
* @return retorna ERROR en caso de fallo u OK en caso contrario.
* @author José Miguel Nicolás García
*/
STATUS inventory_set_maxObj(BackPack *backPack, int maxObj);
/**
* @brief Cambia el conjunto del inventario.
* @param backPack puntero a inventario.
* @param set nuevo set 
* @return retorna ERROR en caso de fallo u OK en caso contrario.
* @author José Miguel Nicolás García
*/

STATUS inventory_set_set(BackPack *backPack, Set *set);
/**
* @brief Devuelve la cantidad actual de objetos
* @param backPack puntero a inventario.
* @return retorna -1 en caso de fallo o la cantidad en caso contrario.
* @author José Miguel Nicolás García
*/
int inventory_num_obj(BackPack *backPack);

/**
* @brief Imprime el inventario
* @param backPack puntero a inventario.
* @author José Miguel Nicolás García
*/
void inventory_print(BackPack *backPack);

/**
* @brief Comprueba si el inventario tiene un objeto
* @param backPack puntero a inventario.
* @param object id del objeto a comprobar
* @return TRUE en caso de contenerlo y FALSE en caso contrario
* @author José Miguel Nicolás García
*/
BOOL inventory_have_id(BackPack *backPack, Id object);

/**
* @brief Devuelve los ids almacenados en el inventario (en el set del inventario).
* @param backPack puntero a inventario.
* @return NULL en caso de fallo que el set del inventario no tenga objetos o un array de ids en caso de que los haya.
* @author Alejandro Jerez
*/
Id *inventory_get_objects(BackPack *backPack);
#endif