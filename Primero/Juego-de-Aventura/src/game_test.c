/**
 * @brief It tests game module
 *
 * @file game_test.c
 * @author Javier Fraile Iglesias
 * @version 3.0
 * @date 16-01-2015
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "game.h"
#include "game_test.h"
#include "test.h"
#include "link.h"
#include "player.h"
#include "object.h"
#include "command.h"
#include "space.h"

#define MAX_TESTS 26 /*!< Numero máximo de test que se realizaran en este modulo */

/**
 * @brief Main function for GAME unit tests.
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
    printf("Running all test for module Game:\n");
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
    test1_game_create();
  if (all || test == 2)
    test2_game_create();
  if (all || test == 3)
    test1_game_create_from_file();
  if (all || test == 4)
    test2_game_create_from_file();
  if (all || test == 5)
    test1_game_get_space();
  if (all || test == 6)
    test2_game_get_space();
  if (all || test == 7)
    test1_game_get_player();
  if (all || test == 8)
    test2_game_get_player();
  if (all || test == 9)
    test1_game_get_player_location();
  if (all || test == 10)
    test2_game_get_player_location();
  if (all || test == 11)
    test1_game_get_object_at_name();
  if (all || test == 12)
    test2_game_get_object_at_name();
  if (all || test == 13)
    test1_game_get_link_at_id();
  if (all || test == 14)
    test2_game_get_link_at_id();
  if (all || test == 15)
    test1_game_get_object_at_id();
  if (all || test == 16)
    test2_game_get_object_at_id();
  if (all || test == 17)
    test1_game_get_object_at_position();
  if (all || test == 18)
    test2_game_get_object_at_position();
  if (all || test == 19)
    test1_game_add_space();
  if (all || test == 20)
    test2_game_add_space();
  if (all || test == 21)
    test1_game_add_object();
  if (all || test == 22)
    test2_game_add_object();
  if (all || test == 23)
    test1_game_add_player();
  if (all || test == 24)
    test2_game_add_player();
  if (all || test == 25)
    test1_game_add_link();
  if (all || test == 26)
    test2_game_add_link();

  PRINT_PASSED_PERCENTAGE;

  return 1;
}

void test1_game_create()
{
  int result;
  Game *g;
  g = game_create();
  result = g != NULL;
  PRINT_TEST_RESULT(result);
  game_destroy(g);
}

void test2_game_create()
{
  Game *g;
  Space *s;

  g = game_create();
  s = space_create(1);

  game_add_space(g, s);
  PRINT_TEST_RESULT(game_get_space(g, 1) == s);
  game_destroy(g);
}

void test1_game_create_from_file()
{
  Game *g;
  g = game_create();

  PRINT_TEST_RESULT(game_create_from_file(g, "data.dat") == OK);
  game_destroy(g);
}

void test2_game_create_from_file()
{
  Game *g;
  g = game_create();

  PRINT_TEST_RESULT(game_create_from_file(g, "noexiste.dat") == ERROR);
  game_destroy(g);
}

void test1_game_get_space()
{
  Game *g;
  Space *s1, *s2;
  g = game_create();
  s1 = space_create(1);
  s2 = space_create(2);
  game_add_space(g, s1);
  game_add_space(g, s2);

  PRINT_TEST_RESULT(game_get_space(g, space_get_id(s1)) == s1);
  game_destroy(g);
}

void test2_game_get_space()
{
  Game *g;
  Space *s;
  g = game_create();
  s = space_create(2);

  game_add_space(g, s);
  PRINT_TEST_RESULT(game_get_space(g, 1) == NULL);
  game_destroy(g);
}

void test1_game_get_player()
{
  Game *g;
  Player *p;
  g = game_create();
  p = player_create(1);
  game_add_player(g, p);

  PRINT_TEST_RESULT(game_get_player(g) == p);
  game_destroy(g);
}

void test2_game_get_player()
{
  Game *g;
  Player *p;
  g = game_create();
  p = player_create(1);

  PRINT_TEST_RESULT(game_get_player(g) == NULL);
  game_destroy(g);
  player_destroy(p);
}

void test1_game_get_player_location()
{
  Game *g;
  Player *p;

  p = player_create(1);
  g = game_create();

  game_add_player(g, p);
  PRINT_TEST_RESULT(game_get_player_location(g) == player_get_location(p));
  game_destroy(g);
}

void test2_game_get_player_location()
{
  Game *g;
  Player *p;

  p = player_create(1);
  g = game_create();

  PRINT_TEST_RESULT(game_get_player_location(g) == NO_ID);
  game_destroy(g);
  player_destroy(p);
}

void test1_game_get_object_location()
{
  Game *g;
  Space *s;
  Object *o;

  s = space_create(1);
  o = object_create(1);
  space_set_object(s, object_get_id(o));
  g = game_create();

  game_add_space(g, s);
  PRINT_TEST_RESULT(game_get_object_location(g, object_get_id(o)) == 1);
  game_destroy(g);
}

void test2_game_get_object_location()
{
  Game *g;
  Object *o;

  o = object_create(1);
  g = game_create();

  PRINT_TEST_RESULT(game_get_object_location(g, object_get_id(o)) == NO_ID);
  game_destroy(g);
}

void test1_game_get_link_at_id()
{
  Game *g;
  Link *l;

  l = link_create(1);
  g = game_create();

  game_add_link(g, l);

  PRINT_TEST_RESULT(game_get_link_at_id(g, link_get_id(l)) == l);
  game_destroy(g);
}

void test2_game_get_link_at_id()
{
  Game *g;
  Link *l;

  l = link_create(1);
  g = game_create();

  PRINT_TEST_RESULT(game_get_link_at_id(g, link_get_id(l)) == NULL);
  game_destroy(g);
  link_destroy(l);
}

void test2_game_get_object_at_name()
{
  Game *g;
  Object *o1, *o2;

  o1 = object_create(1);
  o2 = object_create(2);
  g = game_create();

  object_set_name(o1, "objeto1");
  object_set_name(o2, "objeto2");

  game_add_object(g, o1);
  game_add_object(g, o2);

  PRINT_TEST_RESULT(game_get_object_at_name(g, "objeto2") == o2);

  game_destroy(g);
}

void test1_game_get_object_at_name()
{
  Game *g;
  Object *o;

  o = object_create(1);
  g = game_create();

  object_set_name(o, "objeto1");
  game_add_object(g, o);

  PRINT_TEST_RESULT(game_get_object_at_name(g, NULL) == NULL);
  game_destroy(g);
}

void test1_game_get_object_at_id()
{
  Game *g;
  Object *o1, *o2;

  o1 = object_create(1);
  o2 = object_create(2);
  g = game_create();

  game_add_object(g, o1);
  game_add_object(g, o2);

  PRINT_TEST_RESULT(game_get_object_at_id(g, 2) == o2);
  game_destroy(g);
}

void test2_game_get_object_at_id()
{
  Game *g;
  Object *o1, *o2;

  o1 = object_create(1);
  o2 = object_create(2);
  g = game_create();

  game_add_object(g, o1);
  game_add_object(g, o2);

  PRINT_TEST_RESULT(game_get_object_at_id(g, 2) != o1);
  game_destroy(g);
}

void test1_game_get_object_at_position()
{
  Game *g;
  Object *o1, *o2;

  o1 = object_create(1);
  o2 = object_create(2);

  g = game_create();

  game_add_object(g, o1);
  game_add_object(g, o2);

  PRINT_TEST_RESULT(game_get_object_at_position(g, 1) == o2);
  game_destroy(g);
}

void test2_game_get_object_at_position()
{
  Game *g;
  Object *o1, *o2;

  o1 = object_create(1);
  o2 = object_create(2);

  g = game_create();

  game_add_object(g, o2);

  PRINT_TEST_RESULT(game_get_object_at_position(g, 0) != o1);
  game_destroy(g);
  object_destroy(o1);
}

void test1_game_add_space()
{
  Game *g;
  Space *s;

  g = game_create();
  s = space_create(1);

  PRINT_TEST_RESULT(game_add_space(g, s) == OK);
  game_destroy(g);
}

void test2_game_add_space()
{
  Game *g;
  Space *s = NULL;

  g = game_create();

  PRINT_TEST_RESULT(game_add_space(g, s) == ERROR);
  game_destroy(g);
}

void test1_game_add_object()
{
  Game *g;
  Object *o;

  g = game_create();
  o = object_create(1);

  PRINT_TEST_RESULT(game_add_object(g, o) == OK);
  game_destroy(g);
}

void test2_game_add_object()
{
  Game *g;
  Object *o = NULL;

  g = game_create();

  PRINT_TEST_RESULT(game_add_object(g, o) == ERROR);
  game_destroy(g);
}

void test1_game_add_player()
{
  Game *g;
  Player *p;

  g = game_create();
  p = player_create(1);

  PRINT_TEST_RESULT(game_add_player(g, p) == OK);
  game_destroy(g);
}

void test2_game_add_player()
{
  Game *g;
  Player *p = NULL;

  g = game_create();

  PRINT_TEST_RESULT(game_add_player(g, p) == ERROR);
  game_destroy(g);
}

void test1_game_add_link()
{
  Game *g;
  Link *l;

  g = game_create();
  l = link_create(1);

  PRINT_TEST_RESULT(game_add_link(g, l) == OK);
  game_destroy(g);
}

void test2_game_add_link()
{
  Game *g;
  Link *l = NULL;

  g = game_create();

  PRINT_TEST_RESULT(game_add_link(g, l) == ERROR);
  game_destroy(g);
}