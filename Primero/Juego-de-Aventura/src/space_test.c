/** 
 * @brief It tests space module
 * 
 * @file space_test.c
 * @author Profesores Pprog
 * @version 3.0 
 * @date 16-01-2015
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "space.h"
#include "space_test.h"
#include "test.h"
#include "object.h"
#include "link.h"

#define MAX_TESTS 52 /*!< Numero máximo de test que se realizaran en este modulo */

/** 
 * @brief Main function for SPACE unit tests. 
 * 
 * You may execute ALL or a SINGLE test
 *   1.- No parameter -> ALL test are executed 
 *   2.- A number means a particular test (the one identified by that number) 
 *       is executed
 *  
 */
int main(int argc, char **argv)
{

  int test = 0;
  int all = 1;

  if (argc < 2)
  {
    printf("Running all test for module Space:\n");
  }
  else
  {
    test = atoi(argv[1]);
    all = 0;
    printf("Running test %d:\t", test);
    if (test < 1 && test > MAX_TESTS)
    {
      printf("Error: unknown test %d\t", test);
      exit(EXIT_SUCCESS);
    }
  }

  if (all || test == 1)
    test1_space_create();
  if (all || test == 2)
    test2_space_create();
  if (all || test == 3)
    test1_space_set_name();
  if (all || test == 4)
    test2_space_set_name();
  if (all || test == 5)
    test3_space_set_name();
  if (all || test == 6)
    test1_space_set_north();
  if (all || test == 7)
    test2_space_set_north();
  if (all || test == 8)
    test1_space_set_south();
  if (all || test == 9)
    test2_space_set_south();
  if (all || test == 10)
    test1_space_set_east();
  if (all || test == 11)
    test2_space_set_east();
  if (all || test == 12)
    test1_space_set_west();
  if (all || test == 13)
    test2_space_set_west();
  if (all || test == 12)
    test1_space_set_up();
  if (all || test == 13)
    test2_space_set_up();
  if (all || test == 12)
    test1_space_set_down();
  if (all || test == 13)
    test2_space_set_down();
  if (all || test == 14)
    test1_space_get_name();
  if (all || test == 15)
    test2_space_get_name();
  if (all || test == 19)
    test1_space_get_north();
  if (all || test == 20)
    test2_space_get_north();
  if (all || test == 21)
    test1_space_get_south();
  if (all || test == 22)
    test2_space_get_south();
  if (all || test == 23)
    test1_space_get_east();
  if (all || test == 24)
    test2_space_get_east();
  if (all || test == 25)
    test1_space_get_west();
  if (all || test == 26)
    test2_space_get_west();
  if (all || test == 27)
    test1_space_get_up();
  if (all || test == 28)
    test2_space_get_up();
  if (all || test == 29)
    test1_space_get_down();
  if (all || test == 30)
    test2_space_get_down();
  if (all || test == 31)
    test1_space_get_id();
  if (all || test == 32)
    test2_space_get_id();
  if (all || test == 33)
    test1_space_object_presence();
  if (all || test == 34)
    test2_space_object_presence();
  if (all || test == 35)
    test1_space_set_gdesc();
  if (all || test == 36)
    test2_space_set_gdesc();
  if (all || test == 37)
    test2_space_get_gdesc();
  if (all || test == 38)
    test2_space_get_gdesc();
  if (all || test == 39)
    test1_space_get_description();
  if (all || test == 40)
    test2_space_get_description();
  if (all || test == 41)
    test1_space_set_iluminado();
  if (all || test == 42)
    test2_space_set_iluminado();
  if (all || test == 43)
    test1_space_get_iluminado();
  if (all || test == 44)
    test2_space_get_iluminado();
  if (all || test == 45)
    test1_space_set_up();
  if (all || test == 46)
    test2_space_set_up();
  if (all || test == 47)
    test1_space_set_down();
  if (all || test == 48)
    test2_space_set_down();
  if (all || test == 49)
    test1_space_get_up();
  if (all || test == 50)
    test2_space_get_up();
  if (all || test == 51)
    test1_space_get_down();
  if (all || test == 52)
    test2_space_get_down();

  PRINT_PASSED_PERCENTAGE;

  return 1;
}

void test1_space_create()
{
  int result;
  Space *s;
  s = space_create(5);
  result = s != NULL;
  PRINT_TEST_RESULT(result);
  space_destroy(s);
}

void test2_space_create()
{
  Space *s;
  s = space_create(4);
  PRINT_TEST_RESULT(space_get_id(s) == 4);
  space_destroy(s);
}

void test1_space_set_name()
{
  Space *s;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_name(s, "hola") == OK);
  space_destroy(s);
}

void test2_space_set_name()
{
  Space *s = NULL;
  PRINT_TEST_RESULT(space_set_name(s, "hola") == ERROR);
}

void test3_space_set_name()
{
  Space *s;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_name(s, NULL) == ERROR);
  space_destroy(s);
}

void test1_space_set_north()
{
  Space *s;
  Id id = 4;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_north(s, id) == OK);
  space_destroy(s);
}

void test2_space_set_north()
{
  Space *s;
  Id id = -1;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_north(s, id) == ERROR);
  space_destroy(s);
}

void test1_space_set_south()
{
  Space *s;
  Id id = 4;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_south(s, id) == OK);
  space_destroy(s);
}

void test2_space_set_south()
{
  Space *s;
  Id id = -1;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_south(s, id) == ERROR);
  space_destroy(s);
}

void test1_space_set_east()
{
  Space *s;
  Id id = 4;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_east(s, id) == OK);
  space_destroy(s);
}

void test2_space_set_east()
{
  Space *s;
  Id id = -1;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_east(s, id) == ERROR);
  space_destroy(s);
}

void test1_space_set_west()
{
  Space *s;
  Id id = 4;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_west(s, id) == OK);
  space_destroy(s);
}

void test2_space_set_west()
{
  Space *s;
  Id id = -1;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_west(s, id) == ERROR);
  space_destroy(s);
}

void test1_space_set_up()
{
  Space *s;
  Id id = 4;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_up(s, id) == OK);
  space_destroy(s);
}

void test2_space_set_up()
{
  Space *s;
  Id id = -1;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_up(s, id) == ERROR);
  space_destroy(s);
}

void test1_space_set_down()
{
  Space *s;
  Id id = 4;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_down(s, id) == OK);
  space_destroy(s);
}

void test2_space_set_down()
{
  Space *s;
  Id id = -1;
  s = space_create(5);
  PRINT_TEST_RESULT(space_set_down(s, id) == ERROR);
  space_destroy(s);
}

void test1_space_get_name()
{
  Space *s;
  s = space_create(1);
  space_set_name(s, "adios");
  PRINT_TEST_RESULT(strcmp(space_get_name(s), "adios") == 0);
  space_destroy(s);
}

void test2_space_get_name()
{
  Space *s = NULL;
  PRINT_TEST_RESULT(space_get_name(s) == NULL);
}

void test1_space_get_north()
{
  Space *s;
  Link *l;
  Id id = 4;
  s = space_create(5);
  l = link_create(4);
  space_set_north(s, id);
  PRINT_TEST_RESULT(space_get_north(s) == link_get_id(l));
  space_destroy(s);
  link_destroy(l);
}

void test2_space_get_north()
{
  Space *s = NULL;
  PRINT_TEST_RESULT(space_get_north(s) == NO_ID);
}

void test1_space_get_south()
{
  Space *s;
  Link *l;
  Id id = 6;
  s = space_create(5);
  l = link_create(6);
  space_set_south(s, id);
  PRINT_TEST_RESULT(space_get_south(s) == link_get_id(l));
  space_destroy(s);
  link_destroy(l);
}

void test2_space_get_south()
{
  Space *s = NULL;
  PRINT_TEST_RESULT(space_get_south(s) == NO_ID);
}

void test1_space_get_east()
{
  Space *s;
  Link *l;
  Id id = 6;
  s = space_create(5);
  l = link_create(6);
  space_set_east(s, id);
  PRINT_TEST_RESULT(space_get_east(s) == link_get_id(l));
  space_destroy(s);
  link_destroy(l);
}
void test2_space_get_east()
{
  Space *s = NULL;
  PRINT_TEST_RESULT(space_get_east(s) == NO_ID);
}

void test1_space_get_west()
{
  Space *s;
  Link *l;
  Id id = 12;
  s = space_create(5);
  l = link_create(12);
  space_set_west(s, id);
  PRINT_TEST_RESULT(space_get_west(s) == link_get_id(l));
  space_destroy(s);
  link_destroy(l);
}

void test2_space_get_west()
{
  Space *s = NULL;
  PRINT_TEST_RESULT(space_get_west(s) == NO_ID);
}

void test1_space_get_up()
{
  Space *s;
  Link *l;
  Id id = 4;
  s = space_create(5);
  l = link_create(4);
  space_set_up(s, id);
  PRINT_TEST_RESULT(space_get_up(s) == link_get_id(l));
  space_destroy(s);
  link_destroy(l);
}

void test2_space_get_up()
{
  Space *s = NULL;
  PRINT_TEST_RESULT(space_get_up(s) == NO_ID);
}

void test1_space_get_down()
{
  Space *s;
  Link *l;
  Id id = 4;
  s = space_create(5);
  l = link_create(4);
  space_set_down(s, id);
  PRINT_TEST_RESULT(space_get_down(s) == link_get_id(l));
  space_destroy(s);
  link_destroy(l);
}

void test2_space_get_down()
{
  Space *s = NULL;
  PRINT_TEST_RESULT(space_get_down(s) == NO_ID);
}

void test1_space_get_id()
{
  Space *s;
  s = space_create(25);
  PRINT_TEST_RESULT(space_get_id(s) == 25);
  space_destroy(s);
}

void test2_space_get_id()
{
  Space *s = NULL;
  PRINT_TEST_RESULT(space_get_id(s) == NO_ID);
}

void test1_space_object_presence()
{
  Space *s;
  s = space_create(7);

  Object *object = NULL;
  object = object_create(3);

  PRINT_TEST_RESULT(space_object_presence(s, object_get_id(object)) == FALSE);
  space_destroy(s);
  object_destroy(object);
}

void test2_space_object_presence()
{
  Space *s;
  s = space_create(4);

  Object *object = NULL;
  object = object_create(1);

  space_set_object(s, object_get_id(object));

  PRINT_TEST_RESULT(space_object_presence(s, object_get_id(object)));
  space_destroy(s);
  object_destroy(object);
}

void test1_space_del_object()
{
  Space *s;
  s = space_create(4);

  Object *object = NULL;
  object = object_create(1);

  space_set_object(s, object_get_id(object));
  space_del_object(s, object_get_id(object));
  PRINT_TEST_RESULT(space_object_presence(s, object_get_id(object)) == FALSE);
  space_destroy(s);
  object_destroy(object);
}
void test2_space_del_object()
{
  Space *s;
  s = space_create(4);

  Object *object = NULL;
  object = object_create(1);

  space_set_object(s, object_get_id(object));

  PRINT_TEST_RESULT(space_del_object(s, object_get_id(object)) == OK);
  space_destroy(s);
  object_destroy(object);
}
void test1_space_set_gdesc()
{
  Space *s;
  s = space_create(4);

  char cadena[6];
  strcpy(cadena, "Hola");

  PRINT_TEST_RESULT(space_set_gdesc(s, cadena, 0) == OK);
  space_destroy(s);
}
void test2_space_set_gdesc()
{
  Space *s;
  s = space_create(4);

  char cadena[6];
  strcpy(cadena, "Hola");

  space_set_gdesc(s, cadena, 0);

  PRINT_TEST_RESULT(strcmp((space_get_gdesc(s, 0)), cadena) == 0);
  space_destroy(s);
}

void test1_space_get_gdesc()
{
  Space *s;
  s = space_create(4);

  char cadena[6];
  strcpy(cadena, "Hi!");

  space_set_gdesc(s, cadena, 1);

  PRINT_TEST_RESULT(space_get_gdesc(s, 1) != NULL);
  space_destroy(s);
}

void test2_space_get_gdesc()
{
  Space *s;
  s = space_create(4);

  char cadena[6];
  strcpy(cadena, "Ey");

  space_set_gdesc(s, cadena, 2);

  PRINT_TEST_RESULT(strcmp((space_get_gdesc(s, 2)), cadena) == 0);
  space_destroy(s);
}

void test1_space_set_object()
{
  Space *s;
  s = space_create(1);
  PRINT_TEST_RESULT(space_set_object(s, TRUE) == OK);
  space_destroy(s);
}

void test2_space_set_object()
{
  Space *s = NULL;
  PRINT_TEST_RESULT(space_set_object(s, TRUE) == ERROR);
}

void test1_space_get_description()
{
  Space *s;

  s = space_create(1);

  PRINT_TEST_RESULT(strlen(space_get_description(s)) == 0);
  space_destroy(s);
}

void test2_space_get_description()
{
  Space *s;
  char cadena[10];

  s = space_create(1);
  strcpy(cadena, "Prueba");
  space_set_description(s, cadena);

  PRINT_TEST_RESULT(strcmp(space_get_description(s), "Prueba") == 0);
  space_destroy(s);
}

void test1_space_set_iluminado()
{
  Space *s = NULL;

  PRINT_TEST_RESULT(space_set_iluminado(s, TRUE) == ERROR);
  space_destroy(s);
}

void test2_space_set_iluminado()
{
  Space *s;

  s = space_create(1);
  PRINT_TEST_RESULT(space_set_iluminado(s, TRUE) == OK);
  space_destroy(s);
}

void test1_space_get_iluminado()
{
  Space *s = NULL;

  PRINT_TEST_RESULT(space_get_iluminado(s) == FALSE);
  space_destroy(s);
}

void test2_space_get_iluminado()
{
  Space *s;

  s = space_create(1);
  space_set_iluminado(s, TRUE);
  PRINT_TEST_RESULT(space_get_iluminado(s) == TRUE);
  space_destroy(s);
}