/** 
 * @brief It tests player module
 * 
 * @file player_test.c
 * @author Alejandro Jerez
 * @version 3.0 
 * @date 28-03-2021
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "player.h"
#include "player_test.h"
#include "test.h"
#include "inventory.h"
#include "set.h"

#define MAX_TESTS 23 /*!< Numero máximo de test que se realizaran en este modulo */

/** 
 * @brief Main function for player unit tests. 
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
    printf("Running all test for module player:\n");
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
    test1_player_create();
  if (all || test == 2)
    test2_player_create();
  if (all || test == 3)
    test1_player_set_name();
  if (all || test == 4)
    test2_player_set_name();
  if (all || test == 5)
    test3_player_set_name();
  if (all || test == 6)
    test1_player_get_id();
  if (all || test == 7)
    test2_player_get_id();
  if (all || test == 8)
    test1_player_get_name();
  if (all || test == 9)
    test2_player_get_name();
  if (all || test == 10)
    test1_player_get_location();
  if (all || test == 11)
    test2_player_get_location();
  if (all || test == 12)
    test1_player_get_num_of_objects();
  if (all || test == 13)
    test2_player_get_num_of_objects();
  if (all || test == 14)
    test1_player_get_objects();
  if (all || test == 15)
    test2_player_get_objects();
  if (all || test == 16)
    test1_player_set_max_inv();
  if (all || test == 17)
    test2_player_set_max_inv();
  if (all || test == 18)
    test1_player_set_location();
  if (all || test == 19)
    test2_player_set_location();
  if (all || test == 20)
    test1_player_set_object();
  if (all || test == 21)
    test2_player_set_object();
  if (all || test == 22)
    test1_player_has_object();
  if (all || test == 23)
    test2_player_has_object();

  PRINT_PASSED_PERCENTAGE;

  return 1;
}

void test1_player_create()
{
  int result;
  Player *p;
  p = player_create(5);
  result = p != NULL;
  PRINT_TEST_RESULT(result);
  player_destroy(p);
}

void test2_player_create()
{
  Player *p;
  p = player_create(4);
  PRINT_TEST_RESULT(player_get_id(p) == 4);
  player_destroy(p);
}

void test1_player_set_name()
{
  Player *p;
  p = player_create(4);
  PRINT_TEST_RESULT(player_set_name(p, "hola") == OK);
  player_destroy(p);
}

void test2_player_set_name()
{
  Player *p = NULL;
  PRINT_TEST_RESULT(player_set_name(p, "hola") == ERROR);
}

void test3_player_set_name()
{
  Player *p;
  p = player_create(4);
  PRINT_TEST_RESULT(player_set_name(p, NULL) == ERROR);
  player_destroy(p);
}

void test1_player_get_id()
{
  Player *p;
  p = player_create(25);
  PRINT_TEST_RESULT(player_get_id(p) == 25);
  player_destroy(p);
}

void test2_player_get_id()
{
  Player *p = NULL;
  PRINT_TEST_RESULT(player_get_id(p) == NO_ID);
}

void test1_player_get_name()
{
  Player *p;
  p = player_create(1);
  player_set_name(p, "adios");
  PRINT_TEST_RESULT(strcmp(player_get_name(p), "adios") == 0);
  player_destroy(p);
}

void test2_player_get_name()
{
  Player *p = NULL;
  PRINT_TEST_RESULT(player_get_name(p) == NULL);
}

void test1_player_get_location()
{
  Player *p;
  p = player_create(1);
  player_set_location(p, 5);
  PRINT_TEST_RESULT(player_get_location(p) == 5);
  player_destroy(p);
}

void test2_player_get_location()
{
  Player *p = NULL;
  PRINT_TEST_RESULT(player_get_location(p) == NO_ID);
}

void test1_player_get_num_of_objects()
{
  Player *p;
  p = player_create(1);
  player_set_max_inv(p, 1);
  player_set_object(p, 5);

  PRINT_TEST_RESULT(player_get_num_of_objects(p) == 1);
  player_destroy(p);
}
void test2_player_get_num_of_objects()
{
  Player *p = NULL;
  PRINT_TEST_RESULT(player_get_num_of_objects(p) == -1);
}

void test1_player_get_objects()
{
  Player *p;
  long *result = NULL;

  p = player_create(1);
  player_set_max_inv(p, 3);
  player_set_object(p, 5);
  result = player_get_objects(p);

  PRINT_TEST_RESULT(result != NULL);

  player_destroy(p);
}

void test2_player_get_objects()
{
  Player *p = NULL;
  PRINT_TEST_RESULT(player_get_objects(p) == NULL);
}

void test1_player_set_max_inv()
{
  Player *p;
  p = player_create(5);
  PRINT_TEST_RESULT(player_set_max_inv(p, 4) == OK);
  player_destroy(p);
}

void test2_player_set_max_inv()
{
  Player *p = NULL;
  PRINT_TEST_RESULT(player_set_max_inv(p, 4) == ERROR);
}

void test1_player_set_location()
{
  Player *p;
  p = player_create(5);
  PRINT_TEST_RESULT(player_set_location(p, 5) == OK);
  player_destroy(p);
}

void test2_player_set_location()
{
  Player *p = NULL;
  PRINT_TEST_RESULT(player_set_location(p, 9) == ERROR);
}

void test1_player_set_object()
{
  Player *p;
  p = player_create(1);
  player_set_max_inv(p, 1);

  PRINT_TEST_RESULT(player_set_object(p, 5) == OK);
  player_destroy(p);
}

void test2_player_set_object()
{
  Player *p = NULL;
  PRINT_TEST_RESULT(player_set_object(p, 5) == ERROR);
}

void test1_player_has_object()
{
  Player *p;
  p = player_create(1);
  player_set_max_inv(p, 1);
  player_set_object(p, 5);
  PRINT_TEST_RESULT(player_has_object(p, 5) == TRUE);
  player_destroy(p);
}

void test2_player_has_object()
{
  Player *p = NULL;
  PRINT_TEST_RESULT(player_has_object(p, 5) == FALSE);
}