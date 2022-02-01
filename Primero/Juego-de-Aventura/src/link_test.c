/**
 * @brief It tests link module
 *
 * @file link_test.c
 * @author Profesores Pprog
 * @version 3.0
 * @date 16-01-2015
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "link.h"
#include "link_test.h"
#include "test.h"
#include "types.h"

#define MAX_TESTS 24 /*!< Numero máximo de test que se realizaran en este modulo */

/**
 * @brief Main function for link unit tests.
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
    printf("Running all test for module link:\n");
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
    test1_link_create();
  if (all || test == 2)
    test2_link_create();
  if (all || test == 3)
    test1_link_set_name();
  if (all || test == 4)
    test2_link_set_name();
  if (all || test == 5)
    test3_link_set_name();
  if (all || test == 6)
    test1_link_set_first_id();
  if (all || test == 7)
    test2_link_set_first_id();
  if (all || test == 8)
    test1_link_set_second_id();
  if (all || test == 9)
    test2_link_set_second_id();
  if (all || test == 10)
    test1_link_set_status();
  if (all || test == 11)
    test2_link_set_status();
  if (all || test == 12)
    test1_link_get_name();
  if (all || test == 13)
    test2_link_get_name();
  if (all || test == 14)
    test1_link_get_first_id();
  if (all || test == 15)
    test2_link_get_first_id();
  if (all || test == 16)
    test1_link_get_second_id();
  if (all || test == 17)
    test2_link_get_second_id();
  if (all || test == 18)
    test1_link_get_pair_id();
  if (all || test == 19)
    test2_link_get_pair_id();
  if (all || test == 20)
    test1_link_get_id();
  if (all || test == 21)
    test2_link_get_id();
  if (all || test == 22)
    test1_link_get_status();
  if (all || test == 23)
    test2_link_get_status();
  if (all || test == 24)
    test3_link_get_status();

  PRINT_PASSED_PERCENTAGE;

  return 1;
}

void test1_link_create()
{
  int result;
  Link *l;
  l = link_create(5);
  result = l != NULL;
  PRINT_TEST_RESULT(result);
  link_destroy(l);
}

void test2_link_create()
{
  Link *l;
  l = link_create(4);
  PRINT_TEST_RESULT(link_get_id(l) == 4);
  link_destroy(l);
}

void test1_link_set_name()
{
  Link *l;
  l = link_create(5);
  PRINT_TEST_RESULT(link_set_name(l, "hola") == OK);
  link_destroy(l);
}

void test2_link_set_name()
{
  Link *l = NULL;
  PRINT_TEST_RESULT(link_set_name(l, "hola") == ERROR);
}

void test3_link_set_name()
{
  Link *l;
  l = link_create(5);
  PRINT_TEST_RESULT(link_set_name(l, NULL) == ERROR);
  link_destroy(l);
}

void test1_link_set_first_id()
{
  Link *l;
  l = link_create(5);
  PRINT_TEST_RESULT(link_set_first_id(l, 5) == OK);
  link_destroy(l);
}

void test2_link_set_first_id()
{
  Link *l = NULL;
  PRINT_TEST_RESULT(link_set_first_id(l, 5) == ERROR);
}

void test1_link_set_second_id()
{
  Link *l;
  l = link_create(5);
  PRINT_TEST_RESULT(link_set_first_id(l, 4) == OK);
  link_destroy(l);
}

void test2_link_set_second_id()
{
  Link *l = NULL;
  PRINT_TEST_RESULT(link_set_second_id(l, 4) == ERROR);
}

void test1_link_set_status()
{
  Link *l;
  l = link_create(5);
  PRINT_TEST_RESULT(link_set_status(l, 1) == OK);
  link_destroy(l);
}

void test2_link_set_status()
{
  Link *l;
  l = link_create(6);
  PRINT_TEST_RESULT(link_set_status(l, 10) == ERROR);
  /*!<Al no ser posible el enlace, este no se ha añadido con lo cual se deja cerrado*/
  link_destroy(l);
}

void test1_link_get_name()
{
  Link *l;
  l = link_create(1);
  link_set_name(l, "adios");
  PRINT_TEST_RESULT(strcmp(link_get_name(l), "adios") == 0);
  link_destroy(l);
}

void test2_link_get_name()
{
  Link *l = NULL;
  PRINT_TEST_RESULT(link_get_name(l) == NULL);
}

void test1_link_get_first_id()
{
  Link *l;
  l = link_create(1);
  link_set_first_id(l, 4);
  PRINT_TEST_RESULT(link_get_first_id(l) == 4);
  link_destroy(l);
}

void test2_link_get_first_id()
{
  Link *l = NULL;
  PRINT_TEST_RESULT(link_get_first_id(l) == NO_ID);
}

void test1_link_get_second_id()
{
  Link *l;
  l = link_create(2);
  link_set_second_id(l, 6);
  PRINT_TEST_RESULT(link_get_second_id(l) == 6);
  link_destroy(l);
}

void test2_link_get_second_id()
{
  Link *l = NULL;
  PRINT_TEST_RESULT(link_get_second_id(l) == NO_ID);
}

void test1_link_get_pair_id()
{
  Link *l;
  l = link_create(2);
  link_set_first_id(l, 4);
  link_set_second_id(l, 6);
  PRINT_TEST_RESULT(link_get_pair_id(l, 4) == 6);
  link_destroy(l);
}

void test2_link_get_pair_id()
{
  Link *l;
  l = link_create(1);
  link_set_first_id(l, 3);
  link_set_second_id(l, 5);
  PRINT_TEST_RESULT(link_get_pair_id(l, 5) == 3);
  link_destroy(l);
}

void test1_link_get_id()
{
  Link *l;
  l = link_create(25);
  PRINT_TEST_RESULT(link_get_id(l) == 25);
  link_destroy(l);
}

void test2_link_get_id()
{
  Link *l = NULL;
  PRINT_TEST_RESULT(link_get_id(l) == NO_ID);
}

void test1_link_get_status()
{
  Link *l;
  l = link_create(25);
  link_set_status(l, 1);
  PRINT_TEST_RESULT(link_get_status(l) == CERRADO);
  link_destroy(l);
}

void test2_link_get_status()
{
  Link *l;
  l = link_create(25);
  link_set_status(l, 12); /* Como 12 no se puede poner, no se ha visto modificada la estructura que fue inicializada a 1 (Cerrado) */
  PRINT_TEST_RESULT(link_get_status(l) == CERRADO);
  link_destroy(l);
}

void test3_link_get_status()
{
  Link *l = NULL;
  PRINT_TEST_RESULT(link_get_status(l) == -1);
  link_destroy(l);
}
