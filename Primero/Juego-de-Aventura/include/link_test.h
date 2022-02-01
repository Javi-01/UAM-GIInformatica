/** 
 * @brief It declares the tests for the link module
 * 
 * @file link_test.h
 * @author Profesores Pprog
 * @version 2.0 
 * @date 19-01-2016
 * @copyright GNU Public License
 */

#ifndef LINK_TEST_H
#define LINK_TEST_H

/**
 * @test Test creación enlace
 * @pre link ID 
 * @post Puntero a enlace NO NULL
 */
void test1_link_create();

/**
 * @test Test creación enlace	if (game_get_last_command(game) == INSPECT && space_get_iluminado(game_get_space(game, game_get_player_location(game))) && game_get_last_cmd_status(game) == OK)
 * @pre link ID 
 * @post link_ID == link Id dado
 */
void test2_link_create();

/**
 * @test Test link_set_name
 * @pre String con nombre del enlace
 * @post Ouput==OK 
 */
void test1_link_set_name();

/**
 * @test Test link_set_name
 * @pre puntero a enlace = NULL 
 * @post Output==ERROR
 */
void test2_link_set_name();

/**
 * @test Test link_set_name
 * @pre puntero a link_name = NULL (puntero a link = NON NULL) 
 * @post Output==ERROR
 */
void test3_link_set_name();

/**
 * @test Test link_set_first_id
 * @pre ID del enlace al norte 
 * @post Output==OK
 */
void test1_link_set_first_id();

/**
 * @test Test link_set_first_id
 * @pre puntero a enlace = NULL 
 * @post Output==ERROR
 */
void test2_link_set_first_id();

/**
 * @test Test link_set_second_id
 * @pre ID del enlace al sur 
 * @post Output==OK
 */
void test1_link_set_second_id();

/**
 * @test Test link_set_second_id
 * @pre puntero a enlace = NULL 
 * @post Output==ERROR
 */
void test2_link_set_second_id();

/**
 * @test Test link_set_status
 * @pre estado del enlace (Abierto = 0, Cerrado = 1)
 * @post Output==OK
 */
void test1_link_set_status();

/**
 * @test Test link_set_status
 * @pre estado del enlace (Abierto = 0, Cerrado = 1)
 * @post Output==ERROR
 */
void test2_link_set_status();

/**
 * @test Test link_get_name
 * @pre String con nombre del enlace
 * @post strcmp==0
 */
void test1_link_get_name();

/**
 * @test Test link_get_name
 * @pre puntero a enlace = NULL 
 * @post Output==NULL
 */
void test2_link_get_name();

/**
 * @test Test link_get_first_id
 * @pre ID del primer elemento del enlace 
 * @post Output==4
 */
void test1_link_get_first_id();

/**
 * @test Test link_get_first_id
 * @pre puntero a enlace = NULL 
 * @post Output==NO_ID
 */
void test2_link_get_first_id();

/**
 * @test Test link_get_second_id
 * @pre ID del segundo elemento del enlace
 * @post Output==6
 */
void test1_link_get_second_id();

/**
 * @test Test link_get_south
 * @pre puntero a enlace = NULL 
 * @post Output==NO_ID
 */
void test2_link_get_second_id();

/**
 * @test Test link_get_pair_id
 * @pre ID del enlace pareja del 4
 * @post Output==6
 */
void test1_link_get_pair_id();

/**
 * @test Test link_get_pair_id
 * @pre ID del enlace pareja del 3
 * @post Output==5
 */
void test2_link_get_pair_id();

/**
 * @test Test link_get_status
 * @pre El estado del enlace (Abierto == 0)
 * @post Output==1
 */
void test1_link_get_status();

/**
 * @test Test link_get_status
 * @pre EL estado del enlace (Cerrado != 0 y Cerrado != 1)
 * @post Output==1
 */
void test2_link_get_status();

/**
 * @test Test link_get_status
 * @pre EL estado del enlace (Cerrado != 0 y Cerrado != 1)
 * @post Output==-1
 */
void test3_link_get_status();

/**
 * @test Test link_get_id
 * @pre enlace  
 * @post Output==25
 */
void test1_link_get_id();

/**
 * @test Test link_get_id
 * @pre puntero a enlace = NULL 
 * @post Output==NO_ID
 */
void test2_link_get_id();

#endif
