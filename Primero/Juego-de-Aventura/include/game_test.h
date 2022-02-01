/** 
 * @brief It declares the tests for the game module
 * 
 * @file game_test.h
 * @author Javier Fraile Iglesias
 * @version 2.0 
 * @date 19-01-2016
 * @copyright GNU Public License
 */

#ifndef SPACE_TEST_H
#define SPACE_TEST_H

/**
 * @test Test creación del game
 * @pre Game create
 * @post Puntero a espacio NO NULL
 */
void test1_game_create();

/**
 * @test Test creación del game
 * @pre Game create y Space create, ademas añadimos un espacio
 * @post se obtiene el espacio luego ha creado coreectamente el juego
 */
void test2_game_create();

/**
 * @test Test game_create_from_file
 * @pre Game create y create from file con el nombre del data.dat
 * @post Ouput==OK 
 */
void test1_game_create_from_file();

/**
 * @test Test game_create_from_file
 * @pre Game create y create from file con el nombre del noexiste.dat
 * @post Output==ERROR
 */
void test2_game_create_from_file();

/**
 * @test Test game_get_space
 * @pre Game create y creacion de dos espacios, añade los espacios al juego
 * @post Output==espacio1
 */
void test1_game_get_space();

/**
 * @test Test game_get_space
 * @pre Game create y creacion de un espacios, añade el espacio al juego, pero comprueba otro que no existe
 * @post Output==NULL
 */
void test2_game_get_space();

/**
 * @test Test game_get_player
 * @pre Game create y player create y añade el jugador al juego
 * @post Output==player
 */
void test1_game_get_player();

/**
 * @test Test game_get_player
 * @pre Game create y player create 
 * @post Output==player
 */
void test2_game_get_player();

/**
 * @test Test game_get_object_location
 * @pre Game crate , space create y creacion de un objeto, y se establece el objeto al espacio, y se añade el espacio
 * @post Output==1
 */
void test1_game_get_object_location();

/**
 * @test Test game_get_object_location
 * @pre Game crate y creacion de un objeto
 * @post Output==NO_ID
 */
void test2_game_get_object_location();

/**
 * @test Test game_get_player_location
 * @pre Game create y player create, y añade el jugador al juego
 * @post Output==player_location
 */
void test1_game_get_player_location();

/**
 * @test Test game_get_player_location
 * @pre Game create y player create
 * @post Output==NO_ID
 */
void test2_game_get_player_location();

/**
 * @test Test game_get_link_at_id
 * @pre Game create y link create , añade el link al juego
 * @post Output==l
 */
void test1_game_get_link_at_id();

/**
 * @test Test game_get_link_at_id
 * @pre Game create y link create
 * @post Output==NULL
 */
void test2_game_get_link_at_id();

/**
 * @test Test game_get_object_at_name
 * @pre Game create y object create, y obtiene el nombre nulo, luego no hay
 * @post Output==NULL
 */
void test1_game_get_object_at_name();

/**
 * @test Test game_get_object_at_name
 * @pre Game create y objects create de dos objetos, añade los objetos al juego
 * @post Output==o2
 */
void test2_game_get_object_at_name();

/**
 * @test Test game_get_object_at_id
 * @pre Game create y objects crate, se añaden al juego y se obtiene el del id 2 
 * @post Output==o2
 */
void test1_game_get_object_at_id();

/**
 * @test Test game_get_object_at_id
 * @pre Game create y objects crate, se añaden al juego y se obtiene el del id 2 
 * @post Output!=o1
 */
void test2_game_get_object_at_id();

/**
 * @test Test game_get_object_at_position
 * @pre Game create y objects crate, se añaden al juego y se obtiene el de la posicion 1
 * @post Output==o2
 */
void test1_game_get_object_at_position();

/**
 * @test Test game_get_object_at_position
 * @pre Game create y objects crate, se añaden el 2 y se obtiene el de la posicion 0
 * @post Output!=o1
 */
void test2_game_get_object_at_position();

/**
 * @test Test game_add_space
 * @pre Game create y space create, se añade el espacio al juego
 * @post Output==OK
 */
void test1_game_add_space();

/**
 * @test Test game_add_space
 * @pre Game create y space = NULL y se intenta añadir
 * @post Output==ERROR
 */
void test2_game_add_space();

/**
 * @test Test game_add_object
 * @pre Game create y object create, se añade el objeto al juego 
 * @post Output==OK
 */
void test1_game_add_object();

/**
 * @test Test game_add_object
 * @pre Game create y object = NULL y se intenta añadir
 * @post Output==ERROR
 */
void test2_game_add_object();

/**
 * @test Test game_add_player
 * @pre Game create y player create, se añade el player al juego
 * @post Output==OK
 */
void test1_game_add_player();

/**
 * @test Test game_add_player
 * @pre Game create y player = NULL y se intenta añadir 
 * @post Output==ERROR
 */
void test2_game_add_player();

/**
 * @test Test game_add_link
 * @pre Game create y link create, se añade el link al juego 
 * @post Output==OK
 */
void test1_game_add_link();

/**
 * @test Test game_add_link
 * @pre Game create y link = NULL y se intenta añadir 
 * @post Output==ERROR
 */
void test2_game_add_link();

#endif
