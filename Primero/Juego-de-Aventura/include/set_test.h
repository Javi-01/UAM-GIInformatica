/** 
 * @brief Test para el modulo set
 * 
 * @file set_test.h
 * @author José Miguel Nicolás García
 * @version 1.0 
 * @date 09-03-2021
 * @copyright GNU Public License
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifndef SET_TEST_H
#define SET_TEST_H

/**
 * @test Función de Test 1 para set_create
 * @pre Set creado
 * @post Set!=NULL 
 */
void test1_set_create();
/**
 * @test Función de Test 2 para set_create
 * @pre Set creado 
 * @post Id Objeto != NULL 
 */
void test2_set_create();
/**
 * @test Función de Test 1 para set_add
 * @pre Set creado e Id objeto
 * @post Id Objeto != NULL 
 */
void test1_set_add();
/**
 * @test Función de Test 2 para set_add
 * @pre Set creado e Id objeto
 * @post Id Objeto == NULL
 */
void test2_set_add();
/**
 * @test Función de Test 1 para set_del
 * @pre Set creado e Id objeto
 * @post Id Objeto !=1
 */
void test1_set_del();
/**
 * @test Función de Test 2 para set_del
 * @pre Set creado e Id objeto
 * @post Id Objeto !=NULL
 */
void test2_set_del();
/**
 * @test Función de Test 2 para set_del
 * @pre Set creado e Id objeto
 * @post Id Objeto == Id Objeto
 */
void test1_set_get_id();
/**
 * @test Función de Test 2 para set_del
 * @pre Set creado e Id objeto
 * @post Id Objeto == NULL
 */
void test2_set_get_id();

#endif
