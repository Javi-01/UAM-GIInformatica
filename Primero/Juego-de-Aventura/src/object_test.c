/**
 * @brief It tests player module
 *
 * @file object_test.c
 * @author Alejandro Jerez
 * @version 3.0
 * @date 28-03-2021
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "object.h"
#include "object_test.h"
#include "test.h"
#include "link.h"

#define MAX_TESTS 32 /*!< Numero máximo de test que se realizaran en este modulo */

/**
 * @brief Main function for object unit tests.
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
    printf("Running all test for module object:\n");
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
    test1_object_create();
  if (all || test == 2)
    test2_object_create();
  if (all || test == 3)
    test1_object_get_id();
  if (all || test == 4)
    test2_object_get_id();
  if (all || test == 5)
    test1_object_get_name();
  if (all || test == 6)
    test2_object_get_name();
  if (all || test == 7)
    test1_object_get_description();
  if (all || test == 8)
    test2_object_get_description();
  if (all || test == 9)
    test1_object_set_description();
  if (all || test == 10)
    test1_object_set_description();
  if (all || test == 11)
    test1_object_set_name();
  if (all || test == 12)
    test2_object_set_name();

  if (all || test == 13)
    test1_object_set_link();
  if (all || test == 14)
    test2_object_set_link();
  if (all || test == 15)
    test1_object_open_link();
  if (all || test == 16)
    test2_object_open_link();
  if (all || test == 17)
    test1_object_set_iluminate();
  if (all || test == 18)
    test2_object_set_iluminate();
  if (all || test == 19)
    test1_object_get_iluminate();
  if (all || test == 20)
    test2_object_get_iluminate();
  if (all || test == 21)
    test1_object_set_turnedon();
  if (all || test == 22)
    test2_object_set_turnedon();
  if (all || test == 23)
    test1_object_is_turnedon();
  if (all || test == 24)
    test2_object_is_turnedon();
  if (all || test == 25)
    test1_object_set_movility();
  if (all || test == 26)
    test2_object_set_movility();
  if (all || test == 27)
    test1_object_get_dependency();
  if (all || test == 28)
    test2_object_get_dependency();
  if (all || test == 29)
    test1_object_set_dependency();
  if (all || test == 30)
    test2_object_set_dependency();
  if (all || test == 31)
    test1_object_get_movility();
  if (all || test == 32)
    test2_object_get_movility();

  PRINT_PASSED_PERCENTAGE;

  return 1;
}

void test1_object_create()
{
  int result;
  Object *o;
  o = object_create(5);
  result = o != NULL;
  PRINT_TEST_RESULT(result);
  object_destroy(o);
}

void test2_object_create()
{
  Object *o;
  o = object_create(4);
  PRINT_TEST_RESULT(object_get_id(o) == 4);
  object_destroy(o);
}

void test1_object_get_id()
{
  Object *o;
  o = object_create(25);
  PRINT_TEST_RESULT(object_get_id(o) == 25);
  object_destroy(o);
}

void test2_object_get_id()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_get_id(o) == NO_ID);
}

void test1_object_get_name()
{
  Object *o;
  o = object_create(1);
  object_set_name(o, "adios");
  PRINT_TEST_RESULT(strcmp(object_get_name(o), "adios") == 0);
  object_destroy(o);
}

void test2_object_get_name()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_get_name(o) == NULL);
}

void test1_object_get_description()
{
  Object *o;
  o = object_create(1);
  object_set_description(o, "adios");
  PRINT_TEST_RESULT(strcmp(object_get_description(o), "adios") == 0);
  object_destroy(o);
}

void test2_object_get_description()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_get_description(o) == NULL);
}

void test1_object_set_description()
{
  Object *o;
  o = object_create(4);
  PRINT_TEST_RESULT(object_set_description(o, "hola") == OK);
  object_destroy(o);
}

void test2_object_set_description()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_set_description(o, "hola") == ERROR);
}

void test1_object_set_name()
{
  Object *o;
  o = object_create(4);
  PRINT_TEST_RESULT(object_set_name(o, "hola") == OK);
  object_destroy(o);
}

void test2_object_set_name()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_set_name(o, "hola") == ERROR);
}

void test1_object_set_link()
{

  Object *o;
  Link *l;
  o = object_create(4);
  l = link_create(2);

  PRINT_TEST_RESULT(object_set_link_id(o, 3) == OK);
  object_destroy(o);
  link_destroy(l);
}

void test2_object_set_link()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_set_link_id(o, 3) == ERROR);
}

void test1_object_open_link()
{
  Object *o;
  Link *l;
  o = object_create(4);
  l = link_create(4);
  object_set_link_id(o, 4);

  PRINT_TEST_RESULT(object_open_link(o, 4) == OK);
  object_destroy(o);
  link_destroy(l);
}

void test2_object_open_link()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_open_link(o, 4) == ERROR);
}

void test1_object_set_iluminate()
{
  Object *o;
  o = object_create(4);
  PRINT_TEST_RESULT(object_set_iluminate(o, TRUE) == OK);
  object_destroy(o);
}

void test2_object_set_iluminate()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_set_iluminate(o, FALSE) == ERROR);
}

void test1_object_get_iluminate()
{
  Object *o;
  o = object_create(25);
  object_set_iluminate(o, TRUE);
  PRINT_TEST_RESULT(object_get_iluminate(o) == TRUE);
  object_destroy(o);
}

void test2_object_get_iluminate()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_get_iluminate(o) == FALSE);
}

void test1_object_set_turnedon()
{
  Object *o;
  BOOL aux = FALSE;
  o = object_create(4);
  PRINT_TEST_RESULT(object_set_turnedon(o, aux) == ERROR);
  object_destroy(o);
}

void test2_object_set_turnedon()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_set_turnedon(o, FALSE) == ERROR);
}

void test1_object_is_turnedon()
{
  Object *o;
  o = object_create(25);
  object_set_turnedon(o, FALSE);
  PRINT_TEST_RESULT(object_is_turnedon(o) == FALSE);
  object_destroy(o);
}

void test2_object_is_turnedon()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_is_turnedon(o) == FALSE);
}

void test1_object_set_movility()
{
  Object *o;
  o = object_create(4);
  PRINT_TEST_RESULT(object_set_movility(o, TRUE) == OK);
  object_destroy(o);
}

void test2_object_set_movility()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_set_movility(o, FALSE) == ERROR);
}

void test1_object_get_movility()
{
  Object *o;
  o = object_create(25);
  object_set_movility(o, FALSE);
  PRINT_TEST_RESULT(object_get_mobility(o) == FALSE);
  object_destroy(o);
}

void test2_object_get_movility()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_get_mobility(o) == FALSE);
}

void test1_object_get_dependency()
{
  Object *o;
  Id aux = 4;
  o = object_create(1);
  object_set_dependecy(o, aux);
  PRINT_TEST_RESULT(object_get_dependecy(o) == aux);
  object_destroy(o);
}

void test2_object_get_dependency()
{
  Object *o = NULL;
  PRINT_TEST_RESULT(object_get_dependecy(o) == NO_ID);
}

void test1_object_set_dependency()
{
  Object *o;
  Id aux = 4;
  o = object_create(4);
  PRINT_TEST_RESULT(object_set_dependecy(o, aux) == OK);
  object_destroy(o);
}

void test2_object_set_dependency()
{
  Object *o = NULL;
  Id aux = 4;
  PRINT_TEST_RESULT(object_set_dependecy(o, aux) == ERROR);
}
