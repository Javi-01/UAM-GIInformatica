/**
 * @brief Implementación de la estructura de objetos y sus funciones.
 *
 * @file object.c
 * @author Alejandro Jerez
 * @version 2.0
 * @date 8-04-2021
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "object.h"
#include "types.h"
#include "space.h"

/*!<Object structure*/
struct _Object
{
  Id id;                           /*!< Identificador del objeto */
  char name[WORD_SIZE + 1];        /*!< Nombre del objeto */
  char description[WORD_SIZE + 1]; /*!< Descripción */
  BOOL movable;                    /*!< Indica si un objeto se puede mover o no */
  Id dependency;                   /*!< Identificador del objeto del que se depende */
  Id open;                         /*!< Identificador de link que puede abrir */
  BOOL iluminate;                  /*!< Booleano que indica si el objeto podrá ilummminar o no un espacio */
  BOOL turnedon;                   /*!< Indica si esta encendido o no el objeto */
};

Object *object_create(Id id)
{

  Object *newObject = NULL;

  if (id == NO_ID)
  {
    return NULL;
  }

  newObject = (Object *)malloc(sizeof(Object));

  if (newObject == NULL)
  {
    return NULL;
  }
  newObject->id = id;

  newObject->name[0] = '\0';
  newObject->description[0] = '\0';

  newObject->open = NO_ID;
  newObject->movable = FALSE;
  newObject->dependency = NO_ID;
  newObject->iluminate = FALSE;
  newObject->turnedon = FALSE;

  return newObject;
}

STATUS object_destroy(Object *object)
{

  if (!object)
  {
    return ERROR;
  }

  free(object);
  object = NULL;

  return OK;
}

Id object_get_id(Object *object)
{

  if (!object)
  {
    return NO_ID;
  }
  return object->id;
}

Id object_get_link_id(Object *object)
{

  if (!object)
  {
    return NO_ID;
  }
  return object->open;
}

const char *object_get_name(Object *object)
{

  if (!object)
  {
    return NULL;
  }
  return object->name;
}

const char *object_get_description(Object *object)
{
  if (!object)
  {
    return NULL;
  }
  return object->description;
}

STATUS object_set_name(Object *object, char *name)
{

  if (!object || !name)
  {
    return ERROR;
  }

  if (!strcpy(object->name, name))
  {
    return ERROR;
  }

  return OK;
}

STATUS object_set_description(Object *object, char *description)
{
  if (!object || !description)
  {
    return ERROR;
  }
  if (!strcpy(object->description, description))
  {
    return ERROR;
  }

  return OK;
}

STATUS object_set_link_id(Object *object, Id id_link)
{

  if (!object)
  {
    return ERROR;
  }

  object->open = id_link;

  return OK;
}

STATUS object_open_link(Object *object, Id link_id)
{

  if (!object || link_id == NO_ID)
  {
    return ERROR;
  }

  if (object_get_link_id(object) == link_id)
  {
    return OK;
  }

  return ERROR;
}

STATUS object_set_iluminate(Object *object, BOOL iluminate)
{

  if (!object)
  {
    return FALSE;
  }

  object->iluminate = iluminate;

  return TRUE;
}

BOOL object_get_iluminate(Object *object)
{

  if (!object)
  {
    return FALSE;
  }

  return object->iluminate;
}

BOOL object_is_turnedon(Object *object)
{

  if (!object)
  {
    return FALSE;
  }

  return object->turnedon;
}

STATUS object_set_turnedon(Object *object, BOOL turnedon)
{

  if (!object)
  {
    return FALSE;
  }

  if (object->iluminate == TRUE)
  { /* Si el objeto se puede iluminar, dicho objeto se ilumina o se apaga */
    object->turnedon = turnedon;
    return OK;
  }

  return ERROR;
}

BOOL object_get_mobility(Object *object)
{

  if (!object)
  {
    return FALSE;
  }

  return object->movable;
}

STATUS object_set_movility(Object *object, BOOL movility)
{

  if (!object || !movility)
  {
    return ERROR;
  }

  object->movable = movility;
  return OK;
}

Id object_get_dependecy(Object *object)
{

  if (!object)
  {
    return NO_ID;
  }

  return object->dependency;
}

STATUS object_set_dependecy(Object *object, Id depends)
{

  if (!object || !depends)
  {
    return ERROR;
  }

  object->dependency = depends;
  return OK;
}

STATUS object_print(Object *object)
{

  if (!object)
  {
    return ERROR;
  }

  fprintf(stdout, "--> Object (Id: %ld; Name: %s; Movable: %d; Dependency: %ld Open: %ld Iluminated: %d Turnedon: %d \n", object->id, object->name, object->movable, object->dependency, object->open, object->iluminate, object->turnedon);

  return OK;
}
