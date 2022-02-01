/** 
 * @brief Test para el módulo command
 * 
 * @file command_test.c
 * @author Iván Fernández París
 * @version 1.0 
 * @date 15-04-2021
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "command.h"
#include "command_test.h"

/** 
 * @brief Main function for COMMAND unit tests. 
 * 
 * You may execute introduce the name of the differents 
 * command (in case of request a command is indicated)
 *  
 */
int main(int argc, char **argv)
{

  T_Command command = NO_CMD;
  extern char *cmd_to_str[N_CMD][N_CMDT];

  printf("\nIntroduce el comando Exit o e:\n");
  command = command_get_user_input();
  if (command == EXIT)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Exit o e)\n");
  }

  printf("\nIntroduce el comando Roll o rl:\n");
  command = command_get_user_input();
  if (command == ROLL)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Roll o rl)\n");
  }

  printf("\nIntroduce el comando Take o t:\n");
  command = command_get_user_input();
  if (command == TAKE)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Take o t)\n");
  }

  printf("\nIntroduce el comando Drop o d:\n");
  command = command_get_user_input();
  if (command == DROP)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Drop o d)\n");
  }

  printf("\nIntroduce el comando Move o m:\n");
  command = command_get_user_input();
  if (command == MOVE)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Move o m)\n");
  }

  printf("\nIntroduce el comando Inspect o i:\n");
  command = command_get_user_input();
  if (command == INSPECT)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Inspect o i)\n");
  }

  printf("\nIntroduce el comando Turnon u on:\n");
  command = command_get_user_input();
  if (command == TURNON)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Turnon o on)\n");
  }

  printf("\nIntroduce el comando Turnoff u off:\n");
  command = command_get_user_input();
  if (command == TURNOFF)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Open u off)\n");
  }

  printf("\nIntroduce el comando Open u o:\n");
  command = command_get_user_input();
  if (command == OPEN)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Open u o)\n");
  }

  printf("\nIntroduce el comando Save o s:\n");
  command = command_get_user_input();
  if (command == SAVE)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Save o s)\n");
  }

  printf("\nIntroduce el comando Load o l:\n");
  command = command_get_user_input();
  if (command == LOAD)
  {
    printf("Comando correcto => %s (%s)\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
  }
  else
  {
    printf("El comando introducido no corresponde con el indicado (Load o l)\n");
  }

  printf("\nIntroduce cualquier palabra que no sea uno de los comandos anteriores:\n");
  command = command_get_user_input();
  if (command == UNKNOWN)
  {
    printf("Comando correcto => %s \n", cmd_to_str[command - NO_CMD][CMDL]);
  }
  else
  {
    printf("El comando introducido ya fue regitrado anteriormente\n");
  }

  return 1;
}
