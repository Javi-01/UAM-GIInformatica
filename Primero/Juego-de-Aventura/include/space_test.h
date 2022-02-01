/** 
 * @brief It declares the tests for the space module
 * 
 * @file space_test.h
 * @author Profesores Pprog
 * @version 2.0 
 * @date 19-01-2016
 * @copyright GNU Public License
 */

#ifndef SPACE_TEST_H
#define SPACE_TEST_H

/**
 * @test Test creación espacio
 * @pre Space ID 
 * @post Puntero a espacio NO NULL
 */
void test1_space_create();

/**
 * @test Test creación espacio
 * @pre Space ID 
 * @post Space_ID == Space Id dado
 */
void test2_space_create();

/**
 * @test Test space_set_name
 * @pre String con nombre del espacio
 * @post Ouput==OK 
 */
void test1_space_set_name();

/**
 * @test Test space_set_name
 * @pre puntero a espacio = NULL 
 * @post Output==ERROR
 */
void test2_space_set_name();

/**
 * @test Test space_set_name
 * @pre puntero a space_name = NULL (puntero a space = NON NULL) 
 * @post Output==ERROR
 */
void test3_space_set_name();

/**
 * @test Test space_set_north
 * @pre ID del espacio al norte 
 * @post Output==OK
 */
void test1_space_set_north();

/**
 * @test Test space_set_north
 * @pre puntero a espacio = NULL 
 * @post Output==ERROR
 */
void test2_space_set_north();

/**
 * @test Test space_set_south
 * @pre ID del espacio al sur 
 * @post Output==OK
 */
void test1_space_set_south();

/**
 * @test Test space_set_south
 * @pre puntero a espacio = NULL 
 * @post Output==ERROR
 */
void test2_space_set_south();

/**
 * @test Test space_set_east
 * @pre ID del espacio al este 
 * @post Output==OK
 */
void test1_space_set_east();

/**
 * @test Test space_set_east
 * @pre puntero a espacio = NULL 
 * @post Output==ERROR
 */
void test2_space_set_east();

/**
 * @test Test space_set_west
 * @pre ID del espacio al oeste 
 * @post Output==ERROR
 */
void test1_space_set_west();

/**
 * @test Test space_set_west
 * @pre puntero a espacio = NULL 
 * @post Output==ERROR
 */
void test2_space_set_west();

/**
 * @test Test space_get_name
 * @pre String con nombre del espacio
 * @post strcmp==0
 */
void test1_space_get_name();

/**
 * @test Test space_get_name
 * @pre puntero a espacio = NULL 
 * @post Output==NULL
 */
void test2_space_get_name();

/**
 * @test Test space_get_north
 * @pre ID del espacio al norte 
 * @post Output==link
 */
void test1_space_get_north();

/**
 * @test Test space_get_north
 * @pre puntero a espacio = NULL 
 * @post Output==NO_ID
 */
void test2_space_get_north();

/**
 * @test Test space_get_south
 * @pre ID del espacio al sur 
 * @post Output==link
 */
void test1_space_get_south();

/**
 * @test Test space_get_south
 * @pre puntero a espacio = NULL 
 * @post Output==NO_ID
 */
void test2_space_get_south();

/**
 * @test Test space_get_east
 * @pre ID del espacio al este 
 * @post Output==link
 */
void test1_space_get_east();

/**
 * @test Test space_get_east
 * @pre puntero a espacio = NULL 
 * @post Output==NO_ID
 */
void test2_space_get_east();

/**
 * @test Test space_get_west
 * @pre ID del espacio al oeste
 * @post Output==link
 */
void test1_space_get_west();

/**
 * @test Test space_get_west
 * @pre puntero a espacio = NULL 
 * @post Output==NO_ID
 */
void test2_space_get_west();

/**
 * @test Test space_get_id
 * @pre espacio  
 * @post Output==link
 */
void test1_space_get_id();

/**
 * @test Test space_get_id
 * @pre puntero a espacio = NULL 
 * @post Output==NO_ID
 */
void test2_space_get_id();

/**
 * @test Test space_object_presence
 * @pre espacio  y objeto
 * @post Output==FALSE
 */
void test1_space_object_presence();

/**
 * @test Test space_object_presence
 * @pre espacio  y objeto
 * @post Output==TRUE
 */
void test2_space_object_presence();

/**
 * @test Test space_del_object
 * @pre espacio  y objeto
 * @post Output==FALSE
 */
void test1_space_del_object();

/**
 * @test Test space_del_object
 * @pre espacio  y objeto
 * @post Output==OK
 */
void test2_space_del_object();

/**
 * @test Test space_set_gdesc
 * @pre espacio  y cadena
 * @post Output==OK
 */
void test1_space_set_gdesc();

/**
 * @test Test space_set_gdesc
 * @pre espacio  y cadena
 * @post strcmp==0
 */
void test2_space_set_gdesc();

/**
 * @test Test space_get_gdesc
 * @pre espacio  y cadena
 * @post Output!=NULL
 */
void test1_space_get_gdesc();

/**
 * @test Test space_get_gdesc
 * @pre espacio  y cadena
 * @post strcmp==0
 */
void test2_space_get_gdesc();

/**
 * @test Test space_set_object
 * @pre espacio
 * @post Output==OK
 */
void test1_space_set_object();

/**
 * @test Test space_set_object
 * @pre puntero a espacio = NULL 
 * @post Output==ERROR
 */
void test2_space_set_object();

/**
 * @test Test space_get_object
 * @pre  
 * @post Output==ERROR
 */
void test1_space_get_object();

/**
 * @test Test space_get_object
 * @pre  
 * @post Output==ERROR
 */
void test2_space_get_object();

/**
 * @test Test space_get_description / space_get_long_description
 * @pre space no creado
 * @post Output==0
 */
void test1_space_get_description();

/**
 * @test Test space_get_description / space_get_long_description
 * @pre  space creado
 * @post Output==0
 */
void test2_space_get_description();

/**
 * @test Test space_set_iluminado
 * @pre  spcae no creado
 * @post Output==ERROR
 */
void test1_space_set_iluminado();

/**
 * @test Test space_set_iluminado
 * @pre  space creado
 * @post Output==OK
 */
void test2_space_set_iluminado();

/**
 * @test Test space_get_iluminado
 * @pre space no creado 
 * @post Output==FALSE
 */
void test1_space_get_iluminado();

/**
 * @test Test space_get_iluminado
 * @pre  space creado
 * @post Output==TRUE
 */
void test2_space_get_iluminado();

/**
 * @test Test space_set_up
 * @pre ID del espacio a arriba 
 * @post Output==ERROR
 */
void test1_space_set_up();

/**
 * @test Test space_set_up
 * @pre puntero a espacio = NULL 
 * @post Output==ERROR
 */
void test2_space_set_up();

/**
 * @test Test space_get_up
 * @pre ID del espacio a arriba 
 * @post Output==OK
 */
void test1_space_get_up();

/**
 * @test Test space_get_up
 * @pre puntero a espacio = NULL 
 * @post Output==ERROR
 */
void test2_space_get_up();

/**
 * @test Test space_set_down
 * @pre ID del espacio a abajo
 * @post Output==OK
 */
void test1_space_set_down();

/**
 * @test Test space_set_down
 * @pre puntero a espacio = NULL 
 * @post Output==ERROR
 */
void test2_space_set_down();

/**
 * @test Test space_get_down
 * @pre ID del espacio a abajo 
 * @post Output==OK
 */
void test1_space_get_down();

/**
 * @test Test space_get_down
 * @pre puntero a espacio = NULL 
 * @post Output==ERROR
 */
void test2_space_get_down();

#endif
