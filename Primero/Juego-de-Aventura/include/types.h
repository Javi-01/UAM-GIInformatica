/**
 * @brief It defines common types
 *
 * @file types.h
 * @author Profesores PPROG
 * @version 1.0
 * @date 13-01-2015
 * @copyright GNU Public License
 */

#ifndef TYPES_H
#define TYPES_H

#define WORD_SIZE 1000 /*!< Macro caracteres máximos de una cadena */
#define NO_ID -1       /*!< Macro no hay id */

typedef long Id; /*!< Estructura id */

/** @brief Estructura booleana*/
typedef enum /*!< Estructura booleana */
{
  FALSE, /*!< Salida FALSE */
  TRUE   /*!< Salida TRUE */
} BOOL;  /** @brief Estructura booleana*/

typedef enum /*!< Estructura status */
{
  ERROR,  /*!< Salida ERROR */
  OK      /*!< Salida OK */
} STATUS; /** @brief Estructura status*/

typedef enum /*!< Estructura para ver si el enlace esta cerrado o abierto */
{
  ABIERTO, /*!< Salida ABIERTO */
  CERRADO  /*!< Salida CERRADO */
} LINK;    /** @brief Estructura booleana LINK*/

typedef enum /*!< Estructura direciones */
{
  N,         /*!< Coordenada Norte */
  S,         /*!< Coordenada Sur */
  E,         /*!< Coordenada Este */
  W          /*!< Coordenada Oeste */
} DIRECTION; /** @brief Estructura direciones */

#endif
