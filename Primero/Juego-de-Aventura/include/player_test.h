/**
 * @brief It declares the tests for the player module
 *
 * @file player_test.h
 * @author Aleandro Jerez
 * @version 2.0
 * @date 30-03-2021
 * @copyright GNU Public License
 */

#ifndef PLAYER_TEST_H
#define PLAYER_TEST_H

/**
 * @test Test 1 para player_create
 * @pre player creado
 * @post player!=NULL
 */
void test1_player_create();

/**
 * @test Test 2 para player_create
 * @pre player creado, id=4
 * @post player_get_id == 4
 */
void test2_player_create();

/**
 * @test Test 1 player_set_name
 * @pre String con nombre del jugador
 * @post Ouput==OK
 */
void test1_player_set_name();

/**
 * @test Test 2 player_set_name
 * @pre Player == NULL
 * @post Ouput==ERROR
 */
void test2_player_set_name();

/**
 * @test Test 3 player_set_name
 * @pre String NULL
 * @post Ouput==ERROR
 */
void test3_player_set_name();

/**
 * @test Test 1 player_get_id
 * @pre ID del jugador = 25
 * @post Output==25
 */
void test1_player_get_id();

/**
 * @test Test 2 player_get_id
 * @pre player==NULL
 * @post Output==NO_ID
 */
void test2_player_get_id();

/**
 * @test Test 1 player_get_name
 * @pre nombre del jugador = adios
 * @post Output==(strcmp=0)
 */
void test1_player_get_name();

/**
 * @test Test 2 player_get_name
 * @pre player==NULL
 * @post Output==NULL
 */
void test2_player_get_name();

/**
 * @test Test 1 player_get_location
 * @pre locacizacion del jugador = 5
 * @post Output==5
 */
void test1_player_get_location();

/**
 * @test Test 2 player_get_location
 * @pre player==NULL
 * @post Output==NULL
 */
void test2_player_get_location();

/**
 * @test Test 1 player_get_num_of_objects
 * @pre player_set_obj
 * @post Output==1
 */
void test1_player_get_num_of_objects();

/**
 * @test Test 2 player_get_num_of_objects
 * @pre player==NULL
 * @post Output==NULL
 */
void test2_player_get_num_of_objects();

/**
 * @test Test 1 player_get_objects
 * @pre player_set_obj
 * @post Output!=NULL
 */
void test1_player_get_objects();

/**
 * @test Test 2 player_get_objects
 * @pre player==NULL
 * @post Output==NULL
 */
void test2_player_get_objects();

/**
 * @test Test 1 player_set_max_inv
 * @pre player creado
 * @post Output==OK
 */
void test1_player_set_max_inv();

/**
 * @test Test 2 player_set_max_inv
 * @pre player==NULL
 * @post Output==ERROR
 */
void test2_player_set_max_inv();

/**
 * @test Test 1 player_set_location
 * @pre player creado
 * @post Output==OK
 */
void test1_player_set_location();

/**
 * @test Test 2 player_set_location
 * @pre player==NULL
 * @post Output==ERROR
 */
void test2_player_set_location();

/**
 * @test Test 1 player_set_object
 * @pre player creado y set_max_inv
 * @post Output==OK
 */
void test1_player_set_object();

/**
 * @test Test 2 player_set_object
 * @pre player==NULL
 * @post Output==ERROR
 */
void test2_player_set_object();

/**
 * @test Test 1 player_has_object
 * @pre player creado y set_object
 * @post Output==TRUE
 */
void test1_player_has_object();

/**
 * @test Test 2 player_has_object
 * @pre player==NULL
 * @post Output==FALSE
 */
void test2_player_has_object();

#endif
