/**
 * @brief Modulo de dialogos
 *
 * @file dialogue.c
 * @version 1.0
 * @date 06-04-2021
 * @copyright GNU Public License
 */

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "game.h"
#include "command.h"
#include "dialogue.h"

struct _Dialogue /*!< Estructura del diálogo*/
{
  char command[WORD_SIZE];            /*!< Último comando*/
  char previous_command[WORD_SIZE];   /*!< Comando anterior*/
  char status[WORD_SIZE];             /*!< Último estado*/
  char previous_status[WORD_SIZE];    /*!< Estado anterior*/
  char explicative_phrase[WORD_SIZE]; /*!< Frase explicativa del comando*/
};

/**
* @brief Escoge la frase explicativa correcta
*
* @param dialogue puntero al diálogo
* @param game puntero a game
* @return Retorna ERROR en caso de fallo u OK en caso de éxito.
*
* @author José Miguel Nicolás
*/
void dialogue_explicative_phrase_selection(Dialogue *dialogue, Game *game);

Dialogue *dialogue_create()
{
  int i;
  Dialogue *dialogue = NULL;

  dialogue = (Dialogue *)malloc(sizeof(Dialogue));

  if (!dialogue)
  {
    return NULL;
  }

  for (i = 0; i < WORD_SIZE; i++)
  {
    dialogue->previous_command[i] = 0;
    dialogue->command[i] = 0;
    dialogue->previous_status[i] = 0;
    dialogue->status[i] = 0;
    dialogue->explicative_phrase[i] = 0;
  }
  return dialogue;
}

STATUS dialogue_destroy(Dialogue *dialogue)
{
  if (!dialogue)
  {
    return ERROR;
  }
  free(dialogue);
  return OK;
}

STATUS dialogue_set_command(Dialogue *dialogue, char *command, char *status)
{
  if (!dialogue || !command)
  {
    return ERROR;
  }
  strcpy(dialogue->previous_command, dialogue->command);
  strcpy(dialogue->command, command);
  strcpy(dialogue->previous_status, dialogue->status);
  strcpy(dialogue->status, status);
  return OK;
}
void dialogue_explicative_phrase_selection(Dialogue *dialogue, Game *game)
{

  strcpy(dialogue->explicative_phrase, "Has ejecutado un comando.");

  if (strcmp(dialogue->command, "No command") == 0)
  {
    strcpy(dialogue->explicative_phrase, "");
  }

  if (strcmp(dialogue->command, "Unknown") == 0)
  {
    strcpy(dialogue->explicative_phrase, "Ese comando no es valido, por favor pruebe de nuevo.");
  }
  else if (strcmp(dialogue->previous_status, "ERROR") == 0 && strcmp(dialogue->status, "ERROR") == 0 && strcmp(dialogue->command, dialogue->previous_command) == 0)
  {
    strcpy(dialogue->explicative_phrase, "Ya has intentado eso, prueba otra cosa.");
  }
  else
  {
    if (strcmp(dialogue->status, "OK") == 0)
    {
      if (strcmp(dialogue->command, "Inspect") == 0)
      {
        strcpy(dialogue->explicative_phrase, "Has inspeccionado algo!");
      }
      else if (strcmp(dialogue->command, "Take") == 0)
      {
        strcpy(dialogue->explicative_phrase, "Has cogido: ");
        strcat(dialogue->explicative_phrase, object_get_name(game_get_object_at_id(game, inventory_get_last_object(player_get_backpack(game_get_player(game))))));
      }
      else if (strcmp(dialogue->command, "Drop") == 0)
      {
        strcpy(dialogue->explicative_phrase, "Has lanzado un objeto.");
      }
      else if (strcmp(dialogue->command, "Roll") == 0)
      {
        strcpy(dialogue->explicative_phrase, "Dado lanzado!");
      }
      else if (strcmp(dialogue->command, "Turnoff") == 0)
      {
        strcpy(dialogue->explicative_phrase, "Has apapgado un objeto.");
      }
      else if (strcmp(dialogue->command, "Turnon") == 0)
      {
        strcpy(dialogue->explicative_phrase, "Has encendido un objeto.");
      }
      else if (strcmp(dialogue->command, "Move") == 0)
      {
        strcpy(dialogue->explicative_phrase, "Te has movido. Ahora estas en: ");
        strcat(dialogue->explicative_phrase, space_get_name(game_get_space(game, game_get_player_location(game))));
      }
      else if (strcmp(dialogue->command, "Open") == 0)
      {
        strcpy(dialogue->explicative_phrase, "Has abierto algo. Entraras?.");
      }
      else if (strcmp(dialogue->command, "Save") == 0)
      {
        strcpy(dialogue->explicative_phrase, "La partida ha sido guardada correctamente.");
      }
      else if (strcmp(dialogue->command, "Load") == 0)
      {
        strcpy(dialogue->explicative_phrase, "La partida se ha cargado correctamente.");
      }
    }
    else //caso de error
    {

      if (strcmp(dialogue->command, "Inspect") == 0)
      {
        strcpy(dialogue->explicative_phrase, "No se ha podido inspeccionar eso.");
      }
      else if (strcmp(dialogue->command, "Take") == 0)
      {
        strcpy(dialogue->explicative_phrase, "No puedes coger ese objeto.");
      }
      else if (strcmp(dialogue->command, "Drop") == 0)
      {
        strcpy(dialogue->explicative_phrase, "No se ha podido lanzar ese objeto.");
      }
      else if (strcmp(dialogue->command, "Roll") == 0)
      {
        strcpy(dialogue->explicative_phrase, "El dado no se ha podido lanzar correctamente.");
      }
      else if (strcmp(dialogue->command, "Turnoff") == 0)
      {
        strcpy(dialogue->explicative_phrase, "Ese objeto no se ha podido apagar.");
      }
      else if (strcmp(dialogue->command, "Turnon") == 0)
      {
        strcpy(dialogue->explicative_phrase, "Ese objeto no se ha podido encender.");
      }
      else if (strcmp(dialogue->command, "Move") == 0)
      {
        strcpy(dialogue->explicative_phrase, "No te puedes mover para alla. Prueba en otra direccion.");
      }
      else if (strcmp(dialogue->command, "Open") == 0)
      {
        strcpy(dialogue->explicative_phrase, "No se ha podido abrir.");
      }
      else if (strcmp(dialogue->command, "Save") == 0)
      {
        strcpy(dialogue->explicative_phrase, "La partida NO SE HA GUARDADO correctamente.");
      }
      else if (strcmp(dialogue->command, "Load") == 0)
      {
        strcpy(dialogue->explicative_phrase, "No se ha podido guardar.");
      }
      else
      {
        strcpy(dialogue->explicative_phrase, "El ultimo comando se ha ejecutado mal.");
      }
    }
  }
}

char *dialogue_get_explicative_phrase(void *game, char *command, char *status)
{

  Dialogue *dialogue = NULL;
  dialogue = game_get_dialogue((Game *)game);
  if (!game || !command || !dialogue)
  {
    return ERROR;
  }
  dialogue_set_command(dialogue, command, status);
  dialogue_explicative_phrase_selection(dialogue, game);

  return dialogue->explicative_phrase;
}