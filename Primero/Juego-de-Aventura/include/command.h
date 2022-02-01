/**
 * @brief It implements the command interpreter
 *
 * @file command.h
 * @author Profesores PPROG
 * @version 2.0
 * @date 13-01-2020
 * @copyright GNU Public License
 */

#ifndef COMMAND_H
#define COMMAND_H

#define N_CMDT 2 /*!< Cantidad de formas de introducir los distintos tipos de comandos */
#define N_CMD 13 /*!< Distintos comandos que hay */

typedef enum enum_CmdType /*!< Estructura del tamaño/tipos de los comandos */
{
  CMDS,      /*!< Comandos cortos(CMDShort) */
  CMDL       /*!< Comandos largos(CMDLong) */
} T_CmdType; /*!< Estructura del tamaño/tipos de los comandos */

typedef enum enum_Command /*!< Estructura de los comandos */
{
  NO_CMD = -1, /*!< No hay comando  */
  UNKNOWN,     /*!< Comando no reconocido  */
  EXIT,        /*!< Comando de salida del juego  */
  ROLL,        /*!< Comando moverse para lanzar el dado  */
  TAKE,        /*!< Comando coger objeto  */
  DROP,        /*!< Comando dejar objeto */
  MOVE,        /*!< Comando para mover el jugador */
  INSPECT,     /*!< Comnado para inspeccionar la descripción de un objeto o espacio */
  TURNON,      /*!< Comando para encender objetos */
  TURNOFF,     /*!< Comando para apagar objetos */
  OPEN,        /*!< Comando para abrir links */
  SAVE,        /*!< Comando para guardar la partida */
  LOAD,        /*!< Comando para reabrir la partida */
} T_Command;   /*!< Estructura de los comandos */

/**
* @brief Obtiene el comando que introduce el usuario.
* @return cmd (comando correspondiente al intoducido por el usuario).
*
* @author Profesores PPROG
*/
T_Command command_get_user_input();

#endif