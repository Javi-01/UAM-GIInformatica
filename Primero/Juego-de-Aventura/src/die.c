#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "die.h"
#include "types.h"

struct _Die /*!< Estructura del dado*/
{
  Id id;    /*!< ID del dado*/
  long max; /*!< El número más alto*/
  long min; /*!< El número más bajo */
  long num; /*!< Número sacado */
};

Die *die_create(Id id, long max, long min)
{

  Die *newDie = NULL;

  if (id == NO_ID)
  {
    return NULL;
  }

  newDie = (Die *)malloc(sizeof(Die));

  if (newDie == NULL)
  {
    return NULL;
  }

  newDie->id = id;
  newDie->max = max;
  newDie->min = min;
  newDie->num = 0;

  return newDie;
}

STATUS die_destroy(Die *die)
{
  if (!die)
  {
    return ERROR;
  }

  free(die);
  return OK;
}

STATUS die_roll(Die *die)
{
  if (!die)
  {
    return ERROR;
  }

  die->num = die->min + rand() % (die->max + 1 - die->min);

  if (die->num < die->min || die->num > die->max)
  {
    return ERROR;
  }

  return OK;
}

Id die_get_id(Die *die)
{

  if (!die)
  {
    return NO_ID;
  }
  return die->id;
}

long die_get_max(Die *die)
{

  if (!die)
  {
    return -1;
  }
  return die->max;
}

long die_get_min(Die *die)
{

  if (!die)
  {
    return -1;
  }
  return die->min;
}

long die_get_num(Die *die)
{

  if (!die)
  {
    return -1;
  }
  return die->num;
}

STATUS die_set_max(Die *die, long newMax)
{

  if (!die || !newMax)
  {
    return ERROR;
  }

  die->max = newMax;

  return OK;
}

STATUS die_set_min(Die *die, long newMin)
{

  if (!die || !newMin)
  {
    return ERROR;
  }

  die->min = newMin;

  return OK;
}

STATUS die_print(Die *die)
{

  if (!die)
  {
    return ERROR;
  }

  fprintf(stdout, "Die id: %ld, MaxNmber: %ld, MinNumber: %ld Last roll: %ld\n", die_get_id(die), die_get_max(die), die_get_min(die), die_get_num(die));

  return OK;
}
