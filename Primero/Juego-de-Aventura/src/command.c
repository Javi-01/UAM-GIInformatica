/** 
 * @brief It implements the command interpreter
 * 
 * @file command.c
 * @author Profesores PPROG
 * @version 2.0 
 * @date 13-01-2020 
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <strings.h>

#include "command.h"

#define CMD_LENGHT 30 /*!< Tamaño máximo de la cadena del comando */

char *cmd_to_str[N_CMD][N_CMDT] = {{"", "No command"}, {"", "Unknown"}, {"e", "Exit"}, {"rl", "Roll"}, {"t", "Take"}, {"d", "Drop"}, {"m", "Move"}, {"i", "Inspect"}, {"on", "Turnon"}, {"off", "Turnoff"}, {"o", "Open"}, {"s", "Save"}, {"l", "Load"}}; /*!< Comandos escritos */

T_Command command_get_user_input()
{
  T_Command cmd = NO_CMD;
  char input[CMD_LENGHT] = "";
  int i = UNKNOWN - NO_CMD + 1;

  if (scanf("%s", input) > 0)
  {
    cmd = UNKNOWN;
    while (cmd == UNKNOWN && i < N_CMD)
    {
      if (!strcasecmp(input, cmd_to_str[i][CMDS]) || !strcasecmp(input, cmd_to_str[i][CMDL]))
      {
        cmd = i + NO_CMD;
      }
      else
      {
        i++;
      }
    }
  }

  return cmd;
}