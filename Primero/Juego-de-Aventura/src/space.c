/** 
 * @brief It defines a space
 * 
 * @file space.c
 * @author Profesores PPROG
 * @version 1.0 
 * @date 13-01-2015
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "types.h"
#include "space.h"
#include "set.h"
#include "link.h"

/*!< Space structure*/
struct _Space
{
  Id id;                                 /*!< Identificador del espacio */
  char name[WORD_SIZE + 1];              /*!< Nombre del espacio */
  Id north;                              /*!< Id Link del espacio al norte */
  Id south;                              /*!< Id Link del espacio al sur */
  Id east;                               /*!< Id Link del espacio al este */
  Id west;                               /*!< Id Link del espacio al oeste */
  Id up;                                 /*!< Id Link del espacio hacia arriba */
  Id down;                               /*!< Id Link del espacio hacia abajo */
  Set *object;                           /*!< Conjunto de espacios */
  char gdesc[NUM_GDESC][NUM_CHARACTERS]; /*!< Matriz de 3 filas y o columnas donde se guardan los dibujos */
  char description[WORD_SIZE + 1];       /*!< Descripción */
  char longDescription[WORD_SIZE + 1];   /*!< Descripción detallada*/
  BOOL iluminado;                        /*!< Bandera para ver si un espacio esta o no iluminado */
};

Space *space_create(Id id)
{

  Space *newSpace = NULL;
  int i;

  if (id == NO_ID)
    return NULL;

  newSpace = (Space *)malloc(sizeof(Space));

  if (newSpace == NULL)
  {
    return NULL;
  }
  newSpace->id = id;

  newSpace->name[0] = '\0';

  newSpace->north = NO_ID;
  newSpace->south = NO_ID;
  newSpace->east = NO_ID;
  newSpace->west = NO_ID;
  newSpace->up = NO_ID;
  newSpace->down = NO_ID;

  newSpace->object = set_create();

  for (i = 0; i < NUM_GDESC; i++)
  {
    newSpace->gdesc[i][0] = '\0';
  }

  newSpace->description[0] = '\0';
  newSpace->longDescription[0] = '\0';
  newSpace->iluminado = TRUE;

  return newSpace;
}

STATUS space_destroy(Space *space)
{
  if (!space)
  {
    return ERROR;
  }

  set_destroy(space->object);

  free(space);
  space = NULL;

  return OK;
}

STATUS space_set_name(Space *space, char *name)
{
  if (!space || !name)
  {
    return ERROR;
  }

  if (!strcpy(space->name, name))
  {
    return ERROR;
  }

  return OK;
}

STATUS space_set_description(Space *space, char *description)
{
  if (!space || !description)
  {
    return ERROR;
  }
  if (!strcpy(space->description, description))
  {
    return ERROR;
  }
  return OK;
}

STATUS space_set_longDescription(Space *space, char *description)
{
  if (!space || !description)
  {
    return ERROR;
  }
  if (!strcpy(space->longDescription, description))
  {
    return ERROR;
  }
  return OK;
}

STATUS space_set_north(Space *space, Id id)
{
  if (!space || id == NO_ID)
  {
    return ERROR;
  }

  space->north = id;
  return OK;
}

STATUS space_set_south(Space *space, Id id)
{
  if (!space || id == NO_ID)
  {
    return ERROR;
  }

  space->south = id;
  return OK;
}

STATUS space_set_east(Space *space, Id id)
{
  if (!space || id == NO_ID)
  {
    return ERROR;
  }

  space->east = id;
  return OK;
}

STATUS space_set_west(Space *space, Id id)
{
  if (!space || id == NO_ID)
  {
    return ERROR;
  }

  space->west = id;
  return OK;
}

STATUS space_set_down(Space *space, Id id)
{
  if (!space || id == NO_ID)
  {
    return ERROR;
  }

  space->down = id;
  return OK;
}

STATUS space_set_up(Space *space, Id id)
{
  if (!space || id == NO_ID)
  {
    return ERROR;
  }

  space->up = id;
  return OK;
}

STATUS space_set_object(Space *space, Id id)
{
  if (!space)
  {
    return ERROR;
  }
  set_add(space->object, id);
  return OK;
}

STATUS space_set_iluminado(Space *space, BOOL iluminado)
{
  if (!space)
  {
    return ERROR;
  }

  space->iluminado = iluminado;

  return OK;
}

BOOL space_get_iluminado(Space *space)
{
  if (!space)
  {
    return FALSE;
  }

  return space->iluminado;
}

STATUS space_set_gdesc(Space *space, char *line, int pos)
{
  if (!space || pos < 0)
  {
    return ERROR;
  }

  if (!strcpy(space->gdesc[pos], line))
  {
    return ERROR;
  }
  return OK;
}

const char *space_get_gdesc(Space *space, int pos)
{ //Como tiene que ver con una cadena de caracteres lo hre tratado como get name

  if (!space || pos < 0)
  {
    return NULL;
  }
  return space->gdesc[pos];
}

const char *space_get_name(Space *space)
{
  if (!space)
  {
    return NULL;
  }
  return space->name;
}

Id space_get_id(Space *space)
{
  if (!space)
  {
    return NO_ID;
  }
  return space->id;
}
const char *space_get_description(Space *space)
{
  if (!space)
  {
    return NULL;
  }
  return space->description;
}

const char *space_get_longDescription(Space *space)
{
  if (!space)
  {
    return NULL;
  }
  return space->longDescription;
}

Id space_get_north(Space *space)
{
  if (!space)
  {
    return NO_ID;
  }
  return space->north;
}

Id space_get_south(Space *space)
{
  if (!space)
  {
    return NO_ID;
  }
  return space->south;
}

Id space_get_east(Space *space)
{
  if (!space)
  {
    return NO_ID;
  }
  return space->east;
}

Id space_get_west(Space *space)
{
  if (!space)
  {
    return NO_ID;
  }
  return space->west;
}

Id space_get_up(Space *space)
{
  if (!space)
  {
    return NO_ID;
  }
  return space->up;
}

Id space_get_down(Space *space)
{
  if (!space)
  {
    return NO_ID;
  }
  return space->down;
}

STATUS space_del_object(Space *space, Id id)
{ /**********/

  if (set_del(space->object, id) == ERROR)
    return ERROR;

  return OK;
}

BOOL space_object_presence(Space *space, Id objId)
{
  if (!space || !objId)
  {
    return FALSE;
  }
  if (set_have_id(space->object, objId) == FALSE)
    return FALSE;

  return TRUE;
}

Id *space_get_objects(Space *space)
{
  if (!space)
  {
    return NULL;
  }

  return set_get_ids(space->object);
}

int space_get_num_objects(Space *space)
{
  if (!space)
    return NO_ID;

  return set_get_cantidad(space->object);
}
BOOL space_have_id(Space *space, Id object)
{
  return set_have_id(space->object, object);
}

STATUS space_print(Space *space)
{
  Id idaux = NO_ID;
  int pos = 0, i;

  if (!space)
  {
    return ERROR;
  }

  fprintf(stdout, "--> Space (Id: %ld; Name: %s)\n", space->id, space->name);

  idaux = space_get_north(space);
  if (NO_ID != idaux)
  {
    fprintf(stdout, "---> North link: %ld.\n", idaux);
  }
  else
  {
    fprintf(stdout, "---> No north link.\n");
  }

  idaux = space_get_south(space);
  if (NO_ID != idaux)
  {
    fprintf(stdout, "---> South link: %ld.\n", idaux);
  }
  else
  {
    fprintf(stdout, "---> No south link.\n");
  }

  idaux = space_get_east(space);
  if (NO_ID != idaux)
  {
    fprintf(stdout, "---> East link: %ld.\n", idaux);
  }
  else
  {
    fprintf(stdout, "---> No east link.\n");
  }

  idaux = space_get_west(space);
  if (NO_ID != idaux)
  {
    fprintf(stdout, "---> West link: %ld.\n", idaux);
  }
  else
  {
    fprintf(stdout, "---> No west link.\n");
  }
  if (space->iluminado == TRUE)
  {
    fprintf(stdout, "Espacio iluminado: TRUE");
  }
  else
  {
    fprintf(stdout, "Espacio iluminado: FALSE");
  }

  set_print(space->object);

  if (space_get_gdesc(space, pos) != NULL)
  {
    fprintf(stdout, "---> Picture:\n");
    for (i = 0; i < (NUM_GDESC - 1); i++, pos++)
    {
      fprintf(stdout, "%s\n", space_get_gdesc(space, pos));
    }
  }
  else
  {
    fprintf(stdout, "---> No picture.\n");
  }

  return OK;
}
