/**
 * @brief It defines a screen
 *
 * @file screen.h
 * @author Profesores PPROG
 * @version 2.0
 * @date 07-02-2021
 * @copyright GNU Public License
 */

#ifndef __SCREEN__
#define __SCREEN__

#define SCREEN_MAX_STR 100 /*!< Macro para el máximo tramaño de la pantalla */

typedef struct _Area Area; /*!< Estructura del area */

/**
* @brief Crea la pantalla, si ya hay una creada la destruye.
*
* @author Profesores PPROG
*/
void screen_init();

/**
* @brief Destruye la pantalla.
*
* @author Profesores PPROG
*/
void screen_destroy();

/**
* @brief Pinta la pantalla colores azul, negro y blanco.
*
* @author Profesores PPROG
*/
void screen_paint();

/**
* @brief Inicializa el area que usará la pantalla.
* Retorna el area a usar.
*
* @author Profesores PPROG
*/
Area *screen_area_init(int x, int y, int width, int height);

/**
* @brief Destruye el area
*
* @author Profesores PPROG
*/
void screen_area_destroy(Area *area);

/**
* @brief Limpia y resetea el area
*
* @author Profesores PPROG
*/
void screen_area_clear(Area *area);

/**
* @brief Resetea el cursor a unas coordenadas en concreto
*
* @author Profesores PPROG
*/
void screen_area_reset_cursor(Area *area);

/**
* @brief Imprime una cadena de caracteres en la pantalla
*
* @author Profesores PPROG
*/
void screen_area_puts(Area *area, char *str);

#endif
