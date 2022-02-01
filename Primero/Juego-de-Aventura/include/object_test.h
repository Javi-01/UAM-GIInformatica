/**
 * @brief It declares the tests for the object module
 *
 * @file link_test.h
 * @author Alejandro Jerez
 * @version 2.0
 * @date 18-04-2021
 * @copyright GNU Public License
 */

#ifndef OBJECT_TEST_H
#define OBJECT_TEST_H

/**
 * @test Test 1 para object_create
 * @pre objeto creado
 * @post object!=NULL
 */
void test1_object_create();

/**
 * @test Test 2 para object_create
 * @pre objeto creado, id=4
 * @post object_get_id == 4
 */
void test2_object_create();

/**
 * @test Test 1 object_get_id
 * @pre ID del objeto = 25
 * @post Output==25
 */
void test1_object_get_id();

/**
 * @test Test 2 object_get_id
 * @pre object==NULL
 * @post Output==NO_ID
 */
void test2_object_get_id();

/**
 * @test Test 1 object_get_name
 * @pre nombre del objeto = adios
 * @post Output==(strcmp=0)
 */
void test1_object_get_name();

/**
 * @test Test 2 object_get_name
 * @pre object==NULL
 * @post Output==NULL
 */
void test2_object_get_name();

/**
 * @test Test 1 object_get_description
 * @pre nombre del objeto = adios
 * @post Output==(strcmp=0)
 */
void test1_object_get_description();

/**
 * @test Test 2 object_get_description
 * @pre object==NULL
 * @post Output==NULL
 */
void test2_object_get_description();

/**
 * @test Test 1 object_set_description
 * @pre String con nombre del objeto
 * @post Ouput==OK
 */
void test1_object_set_description();

/**
 * @test Test 2 object_set_description
 * @pre Object == NULL
 * @post Ouput==ERROR
 */
void test2_object_set_description();

/**
 * @test Test 1 object_set_description
 * @pre String con nombre del objeto
 * @post Ouput==OK
 */
void test1_object_set_name();

/**
 * @test Test 2 object_set_description
 * @pre Object == NULL
 * @post Ouput==ERROR
 */
void test2_object_set_name();

/**
 * @test Test 1 object_set_link
 * @pre id de un link
 * @post Ouput==OK
 */
void test1_object_set_link();

/**
 * @test Test 2 object_set_link
 * @pre Object == NULL
 * @post Ouput==ERROR
 */
void test2_object_set_link();

/**
 * @test Test 1 object_open_link
 * @pre id de un link
 * @post Ouput==OK
 */
void test1_object_open_link();

/**
 * @test Test 2 object_open_link
 * @pre Object == NULL
 * @post Ouput==ERROR
 */
void test2_object_open_link();

/**
 * @test Test 1 object_set_iluminate
 * @pre True
 * @post Ouput==OK
 */
void test1_object_set_iluminate();

/**
 * @test Test 2 object_set_iluminate
 * @pre Object == NULL
 * @post Ouput==ERROR
 */
void test2_object_set_iluminate();

/**
 * @test Test 1 object_get_iluminate
 * @pre iluminate del objeto = TRUE
 * @post Output TRUE
 */
void test1_object_get_iluminate();

/**
 * @test Test 2 object_get_iluminate
 * @pre Object == NULL
 * @post Ouput==FALSE
 */
void test2_object_get_iluminate();

/**
 * @test Test 1 object_set_turnedon
 * @pre True
 * @post Ouput==OK
 */
void test1_object_set_turnedon();

/**
 * @test Test 2 object_set_turnedon
 * @pre Object == NULL
 * @post Ouput==ERROR
 */
void test2_object_set_turnedon();

/**
 * @test Test 1 object_is_turnedon
 * @pre turnedon del objeto = FALSE
 * @post Output FALSE
 */
void test1_object_is_turnedon();

/**
 * @test Test 2 object_is_turnedon
 * @pre Object == NULL
 * @post Ouput==FALSE
 */
void test2_object_is_turnedon();

/**
 * @test Test 1 object_set_movility
 * @pre True
 * @post Ouput==OK
 */
void test1_object_set_movility();

/**
 * @test Test 2 object_set_movility
 * @pre Object == NULL
 * @post Ouput==ERROR
 */
void test2_object_set_movility();

/**
 * @test Test 1 object_get_dependecy
 * @pre dependency del objeto = 4
 * @post Output 4
 */
void test1_object_get_dependency();

/**
 * @test Test 2 object_get_despendency
 * @pre Object == NULL
 * @post Ouput==NO_ID
 */
void test2_object_get_dependency();

/**
 * @test Test 1 object_set_dependency
 * @pre object_set_dependency=3
 * @post Ouput==OK
 */
void test1_object_set_dependency();

/**
 * @test Test 2 object_set_dependency
 * @pre Object == NULL
 * @post Ouput==ERROR
 */
void test2_object_set_dependency();

/**
 * @test Test 1 object_get_movility
 * @pre FALSE
 * @post Output FALSE
 */
void test1_object_get_movility();

/**
 * @test Test 2 object_get_movility
 * @pre Object == NULL
 * @post Ouput==NO_ID
 */
void test2_object_get_movility();

#endif
