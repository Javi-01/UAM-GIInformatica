/** 
 * @brief Test para el módulo dado
 * 
 * @file die_test.c
 * @author Profesores Pprog
 * @version 2.0 
 * @date 16-01-2015
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "die.h"
#include "die_test.h"
#include "test.h"

#define MAX_TESTS 16 /*!< Numero máximo de test que se ralizarán en el modulo */

/** 
 * @brief Main function for DIE unit tests. 
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
    printf("Running all test for module Die:\n");
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
    test1_die_create();
  if (all || test == 2)
    test2_die_create();
  if (all || test == 3)
    test3_die_create();
  if (all || test == 4)
    test4_die_create();
  if (all || test == 5)
    test1_die_roll();
  if (all || test == 6)
    test1_die_get_id();
  if (all || test == 7)
    test2_die_get_id();
  if (all || test == 8)
    test1_die_get_max();
  if (all || test == 9)
    test2_die_get_max();
  if (all || test == 10)
    test1_die_get_min();
  if (all || test == 11)
    test2_die_get_min();
  if (all || test == 12)
    test1_die_get_num();
  if (all || test == 13)
    test1_die_set_max();
  if (all || test == 14)
    test2_die_set_max();
  if (all || test == 15)
    test1_die_set_min();
  if (all || test == 16)
    test2_die_set_min();

  PRINT_PASSED_PERCENTAGE;

  return 1;
}

void test1_die_create()
{
  int result;
  Die *d;
  d = die_create(5, 9, 1);
  result = d != NULL;
  PRINT_TEST_RESULT(result);
  die_destroy(d);
}

void test2_die_create()
{
  Die *d = NULL;
  d = die_create(4, 20, 1);
  PRINT_TEST_RESULT(die_get_id(d) == 4);
  die_destroy(d);
}

void test3_die_create()
{
  Die *d = NULL;
  d = die_create(4, 20, 1);
  PRINT_TEST_RESULT(die_get_max(d) == 20);
  die_destroy(d);
}

void test4_die_create()
{
  Die *d = NULL;
  d = die_create(4, 20, 1);
  PRINT_TEST_RESULT(die_get_min(d) == 1);
  die_destroy(d);
}

void test1_die_roll()
{
  Die *d = NULL;
  d = die_create(4, 5, 1);
  PRINT_TEST_RESULT(die_roll(d) == OK);
  die_destroy(d);
}

void test1_die_get_id()
{
  Die *d = NULL;
  d = die_create(20, 15, 1);
  PRINT_TEST_RESULT(die_get_id(d) == 20);
  die_destroy(d);
}

void test2_die_get_id()
{
  Die *d = NULL;
  PRINT_TEST_RESULT(die_get_id(d) == NO_ID);
}

void test1_die_get_max()
{
  Die *d = NULL;
  d = die_create(20, 15, 1);
  PRINT_TEST_RESULT(die_get_max(d) == 15);
  die_destroy(d);
}

void test2_die_get_max()
{
  Die *d = NULL;
  PRINT_TEST_RESULT(die_get_max(d) == -1);
}

void test1_die_get_min()
{
  Die *d = NULL;
  d = die_create(20, 14, 1);
  PRINT_TEST_RESULT(die_get_min(d) == 1);
  die_destroy(d);
}

void test2_die_get_min()
{
  Die *d = NULL;
  PRINT_TEST_RESULT(die_get_min(d) == -1);
}

void test1_die_get_num()
{
  Die *d = NULL;
  d = die_create(4, 5, 1);
  die_roll(d);
  PRINT_TEST_RESULT(die_get_num(d) > 0);
  die_destroy(d);
}

void test1_die_set_max()
{
  Die *d = NULL;
  d = die_create(20, 20, 1);
  die_set_max(d, 12);
  PRINT_TEST_RESULT(die_get_max(d) == 12);
  die_destroy(d);
}

void test2_die_set_max()
{
  Die *d = NULL;
  PRINT_TEST_RESULT(die_set_max(d, 12) == ERROR);
}

void test1_die_set_min()
{
  Die *d = NULL;
  d = die_create(20, 20, 1);
  die_set_min(d, 3);
  PRINT_TEST_RESULT(die_get_min(d) == 3);
  die_destroy(d);
}

void test2_die_set_min()
{
  Die *d = NULL;
  PRINT_TEST_RESULT(die_set_max(d, 12) == ERROR);
}
