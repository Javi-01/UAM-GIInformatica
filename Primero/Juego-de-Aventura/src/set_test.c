/**
 * @brief Test que compprueba la funcionalidad de set
 *
 * @file set_test.c
 * @author José Miguel Nicolás García 
 * @version 1.0
 * @date 09-03-2021
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "set.h"
#include "test.h"
#include "set_test.h"

#define MAX_TESTS 8 /*!< Numero máximo de test que se realizaran en este modulo */

/** 
 * @brief Main function for set unit tests. 
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
        printf("Running all test for module Set:\n");
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
        test1_set_create();
    if (all || test == 2)
        test2_set_create();

    if (all || test == 3)
        test1_set_add();
    if (all || test == 4)
        test2_set_add();
    if (all || test == 5)
        test1_set_del();
    if (all || test == 6)
        test2_set_del();
    if (all || test == 7)
        test1_set_get_id();
    if (all || test == 8)
        test2_set_get_id();

    PRINT_PASSED_PERCENTAGE;

    return 1;
}

void test1_set_create()
{
    int result;
    Set *set;
    set = set_create();
    result = set != NULL;
    PRINT_TEST_RESULT(result);
    set_destroy(set);
}
void test2_set_create()
{
    int result;
    Set *set;
    set = set_create();
    set_add(set, 1);
    result = set_get_id(set, 0) != NO_ID;
    PRINT_TEST_RESULT(result);
    set_destroy(set);
}
/*
void test2_set_destroy() //Para comprobar la funcion destroy mejor usar valgrind*/
void test1_set_add()
{
    int result;
    Set *set;
    set = set_create();
    set_add(set, 1);
    result = set_get_id(set, 0) != NO_ID;
    PRINT_TEST_RESULT(result);
    set_destroy(set);
}
void test2_set_add()
{
    int result;
    Set *set;
    set = set_create();
    set_add(set, 1);
    result = set_get_id(set, 0) == 1;
    PRINT_TEST_RESULT(result);
    set_destroy(set);
}
void test1_set_del()
{
    int result;
    Set *set;
    set = set_create();
    set_add(set, 1);
    set_del(set, 1);
    result = set_get_id(set, 0) != 1;
    PRINT_TEST_RESULT(result);
    set_destroy(set);
}
void test2_set_del()
{
    int result;
    Set *set;
    set = set_create();
    set_add(set, 1);
    set_del(set, 1);
    result = set_get_id(set, 0) == NO_ID;
    PRINT_TEST_RESULT(result);
    set_destroy(set);
}
void test1_set_get_id()
{
    int result;
    Set *set;
    set = set_create();
    set_add(set, 4);
    result = set_get_id(set, 0) == 4;
    PRINT_TEST_RESULT(result);
    set_destroy(set);
}

void test2_set_get_id()
{
    int result;
    Set *set;
    set = set_create();
    set_add(set, 4);
    result = set_get_id(set, 3) == NO_ID;
    PRINT_TEST_RESULT(result);
    set_destroy(set);
}