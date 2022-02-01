/** 
 * @brief It declares the tests for the inventory module
 * 
 * @file inventory_test.h
 * @author Profesores Pprog
 * @version 2.0 
 * @date 19-01-2016
 * @copyright GNU Public License
 */

#ifndef SPACE_TEST_H
#define SPACE_TEST_H

/**
 * @test Test inventory_create
 * @pre Id inventory 
 * @post Id!=null 
 */
void test1_inventory_create();
/**
 * @test Test inventory_create
 * @pre Id inventory  Id obj
 * @post  result!= NO_ID
 */
void test2_inventory_create();

/**
 * @test Test inventory_add_object
 * @pre Id obj
 * @post  result!= NO_ID
 */
void test1_inventory_add_object();

/**
 * @test Test inventory_add_object
 * @pre Id obj
 * @post  result== Id obj
 */
void test2_inventory_add_object();

/**
 * @test Test inventory_remove_object
 * @pre Id obj
 * @post  result!= Id obj
 */
void test1_inventory_remove_object();

/**
 * @test Test inventory_remove_object
 * @pre Id obj
 * @post  result== NO_ID
 */
void test2_inventory_remove_object();

/**
 * @test Test inventory_get_object
 * @pre Id obj
 * @post  result!= NO_ID
 */
void test1_inventory_get_object();

/**
 * @test Test inventory_get_object
 * @pre Id obj
 * @post  result== Id obj
 */
void test2_inventory_get_object();

/**
 * @test Test inventory_get_maxObj
 * @pre Id inventary
 * @post  result!= UNDIFINED_SIZE (-1)
 */
void test1_inventory_get_maxObj();

/**
 * @test Test inventory_get_maxObj
 * @pre Id inventary
 * @post  result== 4
 */
void test2_inventory_get_maxObj();

/**
 * @test Test inventory_get_set
 * @pre Id inventary
 * @post  result!= NULL
 */
void test1_inventory_get_set();

/**
 * @test Test inventory_get_set
 * @pre Id inventary Id set
 * @post  result== Id set
 */
void test2_inventory_get_set();

/**
 * @test Test inventory_set_maxObj
 * @pre Id inventary
 * @post  result!= UNDIFINED_SIZE
 */
void test1_inventory_set_maxObj();

/**
 * @test Test inventory_set_maxObj
 * @pre Id inventary
 * @post  result== 2
 */
void test2_inventory_set_maxObj();

/**
 * @test Test inventory_num_obj
 * @pre Id inventary
 * @post  result== 0
 */
void test1_inventory_num_obj();

/**
 * @test Test inventory_num_obj
 * @pre Id inventary
 * @post  result== 1
 */
void test2_inventory_num_obj();

/**
 * @test Test inventory_num_obj
 * @pre Id inventary
 * @post  result== FALSE
 */
void test1_inventory_have_id();

/**
 * @test Test inventory_num_obj
 * @pre Id inventary
 * @post  result== TRUE
 */
void test2_inventory_have_id();

#endif
