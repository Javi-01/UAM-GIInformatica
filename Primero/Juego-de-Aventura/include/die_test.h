/** 
 * @brief Test para el módulo dado
 * 
 * @file die_test.h
 * @author Alejandro Jerez Reinoso
 * @version 2.0 
 * @date 10-03-2021
 * @copyright GNU Public License
 */

#ifndef DIE_TEST_H
#define DIE_TEST_H

/**
 * @test Test creación dado
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post Puntero a dado NO NULL
 */
void test1_die_create();

/**
 * @test Test creación dado
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post ID del dado
 */
void test2_die_create();

/**
 * @test Test creación dado
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post NUM_MAX del dado
 */
void test3_die_create();
/**
 * @test Test creación dado
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post NUM_MIN del dado
 */
void test4_die_create();

/** 
 * @test Test roll dado
 * @pre Dado creado
 * @post OK
 */
void test1_die_roll();

/**
 * @test Test get ID del dado
 * @pre Dado creado con ID 20
 * @post ID del dado, 20
 */
void test1_die_get_id();

/**
 * @test Test get ID del dado
 * @pre No se crea un dado
 * @post NO_ID 
 */
void test2_die_get_id();

/**
 * @test Test get max del dado
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post NUM_MAX 
 */
void test1_die_get_max();

/**
 * @test Test get max del dado
 * @pre No se crea un dado
 * @post -1
 */
void test2_die_get_max();

/**
 * @test Test get min del dado
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post NUM_MIM  
 */
void test1_die_get_min();

/**
 * @test Test get min del dado
 * @pre No se crea un dado
 * @post -1
 */
void test2_die_get_min();

/**
 * @test Test get num del dado
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post NUM  
 */
void test1_die_get_num();
/**
 * @test Test set max dado
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post Nuevo MAX_NUM
 */
void test1_die_set_max();
/**
 * @test Test set max dado
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post ERROR
 */
void test2_die_set_max();
/**
 * @test Test set min dado
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post Nuevo NUM_MIN
 */
void test1_die_set_min();
/**
 * @test Test set min
 * @pre Dado con ID, NUM_MAX y NUM_MIN
 * @post ERROR
 */
void test2_die_set_min();

#endif
