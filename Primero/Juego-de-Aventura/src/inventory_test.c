/**
 * @brief Test que compprueba la funcionalidad de inventory
 *
 * @file inventory_test.c
 * @author José Miguel Nicolás García 
 * @version 1.0
 * @date 09-03-2021
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "inventory.h"
#include "test.h"
#include "inventory_test.h"
#include "object.h"
#include "set.h"

#define MAX_TESTS 17 /*!< Numero máximo de test que se realizaran en este modulo */

/** 
 * @brief Main function for inventory unit tests. 
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
        printf("Running all test for module Inventory:\n");
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
        test1_inventory_create();
    if (all || test == 2)
        test2_inventory_create();

    if (all || test == 3)
        test1_inventory_add_object();
    if (all || test == 4)
        test2_inventory_add_object();
    if (all || test == 5)
        test1_inventory_remove_object();
    if (all || test == 6)
        test2_inventory_remove_object();
    if (all || test == 7)
        test1_inventory_get_object();
    if (all || test == 8)
        test2_inventory_get_object();
    if (all || test == 9)
        test1_inventory_get_maxObj();
    if (all || test == 10)
        test1_inventory_get_maxObj();
    if (all || test == 11)
        test2_inventory_get_maxObj();
    if (all || test == 12)
        test1_inventory_get_set();
    if (all || test == 13)
        test2_inventory_get_set();
    if (all || test == 14)
        test1_inventory_set_maxObj();
    if (all || test == 15)
        test2_inventory_set_maxObj();
    if (all || test == 16)
        test1_inventory_num_obj();
    if (all || test == 17)
        test2_inventory_num_obj();
    if (all || test == 17)
        test1_inventory_have_id();
    if (all || test == 17)
        test2_inventory_have_id();

    PRINT_PASSED_PERCENTAGE;

    return 1;
}

void test1_inventory_create()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();
    result = newBackPack != NULL;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
}
void test2_inventory_create()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    Object *obj = object_create(3);
    inventory_set_maxObj(newBackPack, 3);
    inventory_add_object(newBackPack, object_get_id(obj));

    result = inventory_get_object(newBackPack, 0) != NO_ID;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
    object_destroy(obj);
}

void test1_inventory_add_object()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    Object *obj = object_create(7);
    inventory_set_maxObj(newBackPack, 7);
    inventory_add_object(newBackPack, object_get_id(obj));

    result = inventory_get_object(newBackPack, 0) != NO_ID;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
    object_destroy(obj);
}
void test2_inventory_add_object()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    Object *obj = object_create(7);
    inventory_set_maxObj(newBackPack, 4);
    inventory_add_object(newBackPack, object_get_id(obj));

    result = inventory_get_object(newBackPack, 0) == 7;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
    object_destroy(obj);
}
void test1_inventory_remove_object()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    Object *obj = object_create(2);
    inventory_set_maxObj(newBackPack, 4);
    inventory_add_object(newBackPack, object_get_id(obj));

    inventory_remove_object(newBackPack, 2);

    result = inventory_get_object(newBackPack, 0) != 2;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
    object_destroy(obj);
}
void test2_inventory_remove_object()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    Object *obj = object_create(5);
    inventory_set_maxObj(newBackPack, 3);
    inventory_add_object(newBackPack, object_get_id(obj));

    inventory_remove_object(newBackPack, 5);

    result = inventory_get_object(newBackPack, 0) == NO_ID;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
    object_destroy(obj);
}
void test1_inventory_get_object()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    Object *obj = object_create(3);
    inventory_set_maxObj(newBackPack, 4);
    inventory_add_object(newBackPack, object_get_id(obj));

    result = inventory_get_object(newBackPack, 0) != NO_ID;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
    object_destroy(obj);
}

void test2_inventory_get_object()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    Object *obj = object_create(5);
    inventory_set_maxObj(newBackPack, 6);
    inventory_add_object(newBackPack, object_get_id(obj));

    result = inventory_get_object(newBackPack, 0) == 5;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
    object_destroy(obj);
}

void test1_inventory_get_maxObj()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    inventory_set_maxObj(newBackPack, 8);

    result = inventory_get_maxObj(newBackPack) != UNDIFINED_SIZE;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
}

void test2_inventory_get_maxObj()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    inventory_set_maxObj(newBackPack, 4);

    result = inventory_get_maxObj(newBackPack) == 4;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
}

void test1_inventory_get_set()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    result = inventory_get_set(newBackPack) != NULL;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
}

void test2_inventory_get_set()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    Object *obj = object_create(5);
    inventory_set_maxObj(newBackPack, 6);
    inventory_add_object(newBackPack, object_get_id(obj));

    result = set_get_cantidad(inventory_get_set(newBackPack)) == 1;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
    object_destroy(obj);
}

void test1_inventory_set_maxObj()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    inventory_set_maxObj(newBackPack, 3);

    result = inventory_get_maxObj(newBackPack) != UNDIFINED_SIZE;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
}

void test2_inventory_set_maxObj()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    inventory_set_maxObj(newBackPack, 2);

    result = inventory_get_maxObj(newBackPack) == 2;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
}
void test1_inventory_num_obj()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    result = inventory_num_obj(newBackPack) == 0;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
}

void test2_inventory_num_obj()
{
    int result;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    Object *obj = object_create(5);
    inventory_set_maxObj(newBackPack, 6);
    inventory_add_object(newBackPack, object_get_id(obj));

    result = inventory_num_obj(newBackPack) == 1;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
    object_destroy(obj);
}

void test1_inventory_have_id()
{
    int result;
    Id id = 1;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    result = inventory_have_id(newBackPack, id) == FALSE;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
}

void test2_inventory_have_id()
{
    int result;
    Id id = 5;
    BackPack *newBackPack = NULL;
    newBackPack = inventory_create();

    Object *obj = object_create(id);
    inventory_set_maxObj(newBackPack, 6);
    inventory_add_object(newBackPack, object_get_id(obj));

    result = inventory_num_obj(newBackPack) == 1;
    PRINT_TEST_RESULT(result);
    inventory_destroy(newBackPack);
    object_destroy(obj);
}
