/** 
 * @brief It implements the gameManagement module
 * 
 * @file gameManagement.c
 * @author Modificado Javier Fraile Iglesias e Iván Fernández París y José Miguel Nicolás García
 * @version 1.0 
 * @date 11-02-2021 
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "gameManagement.h"
#include "game.h"
#include "link.h"
#include "object.h"
#include "set.h"

STATUS game_management_spaces(Game *game, char *filename)
{

  FILE *file = NULL;
  char line[WORD_SIZE] = "";
  char name[WORD_SIZE] = "";
  char descriptionCorta[WORD_SIZE] = ""; /* !< Campo donde se almacenará la descripción del espacio */
  char descriptionLarga[WORD_SIZE] = ""; /* !< Campo donde se almacenará la descripción del espacio */
  char iluminado[WORD_SIZE] = "";
  char *toks = NULL;
  Id id = NO_ID, north = NO_ID, east = NO_ID, south = NO_ID, west = NO_ID, up = NO_ID, down = NO_ID;
  Space *space = NULL;
  STATUS status = OK;
  char gdesc[NUM_GDESC][NUM_CHARACTERS];
  int i = 0;

  if (!filename)
  {
    return ERROR;
  }

  for (i = 0; i <= NUM_GDESC; i++)
  {
    gdesc[i][0] = '\0';
  }

  file = fopen(filename, "r");
  if (file == NULL)
  {
    return ERROR;
  }

  while (fgets(line, WORD_SIZE, file))
  {
    if (strncmp("#s:", line, 3) == 0)
    {
      toks = strtok(line + 3, "|");
      id = atol(toks);
      toks = strtok(NULL, "|");
      strcpy(name, toks);
      toks = strtok(NULL, "|");
      north = atol(toks);
      toks = strtok(NULL, "|");
      east = atol(toks);
      toks = strtok(NULL, "|");
      south = atol(toks);
      toks = strtok(NULL, "|");
      west = atol(toks);
      toks = strtok(NULL, "|");
      up = atol(toks);
      toks = strtok(NULL, "|");
      down = atol(toks);
      for (i = 0; i < NUM_GDESC; i++)
      {
        toks = strtok(NULL, "|");
        strcpy(gdesc[i], toks);
      }
      toks = strtok(NULL, "|"); /* !< Después de la lectura del diseño de la oca/puente o espacios vacíos se encuentra la descripción de espacio */
      strcpy(descriptionCorta, toks);
      toks = strtok(NULL, "|"); /* !< Después de la lectura del diseño de la oca/puente o espacios vacíos se encuentra la descripción de espacio */
      strcpy(descriptionLarga, toks);
      toks = strtok(NULL, "|");
      strcpy(iluminado, toks); /* !< comprobar si el espacio esta iluminado */

#ifdef DEBUG
      printf("Leido: %ld|%s|%ld|%ld|%ld|%ld|%s|%s|%s|%s|\n", id, name, north, east, south, west, gdesc[TOP_LINE], gdesc[MID_LINE], gdesc[BOT_LINE], description);
#endif

      space = space_create(id);
      if (space != NULL)
      {
        space_set_name(space, name);
        space_set_description(space, descriptionCorta);
        space_set_longDescription(space, descriptionLarga);
        space_set_north(space, link_get_id(game_get_link_at_id(game, north)));
        space_set_east(space, link_get_id(game_get_link_at_id(game, east)));
        space_set_south(space, link_get_id(game_get_link_at_id(game, south)));
        space_set_west(space, link_get_id(game_get_link_at_id(game, west)));
        space_set_up(space, link_get_id(game_get_link_at_id(game, up)));
        space_set_down(space, link_get_id(game_get_link_at_id(game, down)));

        for (i = 0; i < NUM_GDESC; i++)
        {
          space_set_gdesc(space, gdesc[i], i);
        }

        if (strcmp(iluminado, "true") == 0)
        { /* Si se lee que el espacio está iluminado, entocnes lo determinamos en el space */
          space_set_iluminado(space, TRUE);
        }
        else
        {
          space_set_iluminado(space, FALSE);
        }

        game_add_space(game, space);
      }
    }
  }

  if (ferror(file))
  {
    status = ERROR;
  }

  fclose(file);

  return status;
}

STATUS game_management_objects(Game *game, char *filename)
{
  FILE *file = NULL;
  char line[WORD_SIZE] = "";
  char name[WORD_SIZE] = "";
  char description[WORD_SIZE] = "";
  char mobility[WORD_SIZE] = "";
  char iluminate[WORD_SIZE] = "";
  char on[WORD_SIZE] = "";
  char *toks = NULL;
  int i = 0, link_id = 0;
  Object *object = NULL;
  Id location = NO_ID, id = NO_ID, dependency = NO_ID;
  STATUS status = OK;

  if (!filename)
  {
    return ERROR;
  }

  file = fopen(filename, "r");
  if (file == NULL)
  {
    return ERROR;
  }

  while (fgets(line, WORD_SIZE, file))
  {
    if (strncmp("#o:", line, 3) == 0)
    {
      toks = strtok(line + 3, "|");
      id = atol(toks);
      toks = strtok(NULL, "|");
      strcpy(name, toks);
      toks = strtok(NULL, "|");
      location = atol(toks);
      toks = strtok(NULL, "|"); /* !< Después de la lectura de la localización del objeto se encuentra la descripción del mismo */
      strcpy(description, toks);
      toks = strtok(NULL, "|");
      strcpy(mobility, toks); /* !< leemos si el objeto se puede mover */
      toks = strtok(NULL, "|");
      dependency = atol(toks); /* Leemos la dependencia */
      toks = strtok(NULL, "|");
      link_id = atol(toks); /* Leemos el id del link que podra abrir el objeto */
      toks = strtok(NULL, "|");
      strcpy(iluminate, toks);
      toks = strtok(NULL, "|");
      strcpy(on, toks);
#ifdef DEBUG
      printf("Leido: %ld|%s|%ld|%s|\n", id, name, location, description);
#endif

      if (i < MAX_OBJECTS)
      {
        object = object_create(id);
        if (object != NULL)
        {
          object_set_name(object, name);
          object_set_description(object, description);
          object_set_dependecy(object, dependency);
          object_set_link_id(object, link_id);
          space_set_object(game_get_space(game, location), id);

          if (strcmp(mobility, "true") == 0)
          { /* Si se lee que el objeto se puede transportar, entocnes lo determinamos en el objetc */
            object_set_movility(object, TRUE);
          }
          else
          {
            object_set_movility(object, FALSE);
          }

          if (strcmp(iluminate, "true") == 0)
          { /* Si se lee que el objeto se puede iluminar, entocnes lo determinamos en el objetc */
            object_set_iluminate(object, TRUE);
          }
          else
          {
            object_set_iluminate(object, FALSE);
          }

          if (strcmp(on, "true") == 0)
          { /* Si se lee que el objeto esta encendido, entocnes lo determinamos en el objetc */
            object_set_turnedon(object, TRUE);
          }
          else
          {
            object_set_turnedon(object, FALSE);
          }

          game_add_object(game, object);
        }
      }
      i++;
    }
  }

  if (ferror(file))
  {
    status = ERROR;
  }
  fclose(file);

  return status;
}

STATUS game_management_player(Game *game, char *filename)
{

  FILE *file = NULL;
  char line[WORD_SIZE] = "";
  char name[WORD_SIZE] = "";
  char *toks = NULL;
  Id idPlayer = NO_ID, idLocation = NO_ID;
  Player *player = NULL;
  STATUS status = OK;
  int maxInv = 0, num_objs_player = 0, i;

  if (!filename)
  {
    return ERROR;
  }

  file = fopen(filename, "r");
  if (file == NULL)
  {
    return ERROR;
  }
  while (fgets(line, WORD_SIZE, file))
  { /*!< Reads data.dat to create the game board */
    if (strncmp("#p:", line, 3) == 0)
    {
      toks = strtok(line + 3, "|");
      idPlayer = atol(toks);
      toks = strtok(NULL, "|");
      strcpy(name, toks);
      toks = strtok(NULL, "|");
      idLocation = atoi(toks);
      toks = strtok(NULL, "|");
      maxInv = atoi(toks);
      toks = strtok(NULL, "|");
      num_objs_player = atoi(toks);
      char nameObjs[num_objs_player][WORD_SIZE];

      if (num_objs_player > 0 && num_objs_player <= maxInv)
      {

        for (i = 0; i < num_objs_player; i++)
        {
          toks = strtok(NULL, "|");
          if (strlen(toks) > 0)
          {
            strcpy(nameObjs[i], toks);
          }
          else
          {
            break;
          }
        }
      }

#ifdef DEBUG
      printf("Leido: %ld|%s|%ld|%d\n", idPlayer, name, idLocation, maxInv);
#endif

      player = player_create(idPlayer);
      if (player != NULL)
      {
        player_set_name(player, name);
        player_set_location(player, idLocation);
        player_set_max_inv(player, maxInv);
        for (i = 0; i < num_objs_player; i++)
        {
          //space_del_object(game_get_space(game, game_get_object_location(game, object_get_id(game_get_object_at_name(game, nameObjs[i])))), object_get_id(game_get_object_at_name(game, nameObjs[i])));
          player_set_object(player, object_get_id(game_get_object_at_name(game, nameObjs[i])));
        }
        game_add_player(game, player);
      }
    }
  }
  if (ferror(file))
  {
    status = ERROR;
  }

  fclose(file);

  return status;
}

STATUS game_management_links(Game *game, char *filename)
{

  FILE *file = NULL;
  char line[WORD_SIZE] = "";
  char name[WORD_SIZE] = "";
  char *toks = NULL;
  Id id = NO_ID, firstId = NO_ID, secondId = NO_ID;
  Link *link = NULL;
  int linkStatus = 0;
  STATUS status = OK;

  if (!filename)
  {
    return ERROR;
  }

  file = fopen(filename, "r");
  if (file == NULL)
  {
    return ERROR;
  }

  while (fgets(line, WORD_SIZE, file))
  {
    if (strncmp("#l:", line, 3) == 0)
    {
      toks = strtok(line + 3, "|");
      id = atol(toks);
      toks = strtok(NULL, "|");
      strcpy(name, toks);
      toks = strtok(NULL, "|");
      firstId = atol(toks);
      toks = strtok(NULL, "|");
      secondId = atol(toks);
      toks = strtok(NULL, "|");
      linkStatus = atol(toks); /* Estado de abierto o cerrado */
      toks = strtok(NULL, "|");

#ifdef DEBUG
      printf("Leido: %ld|%s|%ld|%ld|%d|\n", id, name, firstId, secondId, linkStatus);
#endif

      link = link_create(id);
      if (link != NULL)
      {
        link_set_name(link, name);
        link_set_first_id(link, firstId);
        link_set_second_id(link, secondId);
        link_set_status(link, linkStatus);
        game_add_link(game, link);
      }
    }
  }

  if (ferror(file))
  {
    status = ERROR;
  }

  fclose(file);

  return status;
}

STATUS game_management_save(Game *game, char *filename)
{

  FILE *file = NULL;
  int i;
  Id *ids;
  char player_inventory[WORD_SIZE] = "";
  char mobility[WORD_SIZE] = "";
  char iluminate[WORD_SIZE] = "";
  char on[WORD_SIZE] = "";

  if (!filename)
  {
    return ERROR;
  }

  file = fopen(filename, "w");
  if (file == NULL)
  {
    return ERROR;
  }

  /* Se guardan los espacios */
  for (i = 1; game_get_space(game, i) != NULL; i++)
  {

    if (space_get_iluminado(game_get_space(game, i)))
      strcpy(iluminate, "true");
    else
      strcpy(iluminate, "false");

    fprintf(file, "#s:%d|%s|%d|%d|%d|%d|%d|%d|%s|%s|%s|%s|%s|%s|%s|\n",
            (int)space_get_id(game_get_space(game, i)), space_get_name(game_get_space(game, i)), (int)space_get_north(game_get_space(game, i)), (int)space_get_east(game_get_space(game, i)),
            (int)space_get_south(game_get_space(game, i)), (int)space_get_west(game_get_space(game, i)),
            (int)space_get_up(game_get_space(game, i)), (int)space_get_down(game_get_space(game, i)), space_get_gdesc(game_get_space(game, i), TOP_LINE), space_get_gdesc(game_get_space(game, i), MID_LINE),
            space_get_gdesc(game_get_space(game, i), BOT_LINE - 1), space_get_gdesc(game_get_space(game, i), BOT_LINE), space_get_description(game_get_space(game, i)), space_get_longDescription(game_get_space(game, i)), iluminate);
  }

  fprintf(file, "\n");

  /* Se guardan los objetos */
  for (i = 1; game_get_object_at_id(game, i) != NULL; i++)
  {

    if (object_get_mobility(game_get_object_at_id(game, i)))
      strcpy(mobility, "true");
    else
      strcpy(mobility, "false");

    if (object_get_iluminate(game_get_object_at_id(game, i)))
      strcpy(iluminate, "true");
    else
      strcpy(iluminate, "false");

    if (object_is_turnedon(game_get_object_at_id(game, i)))
      strcpy(on, "true");
    else
      strcpy(on, "false");

    fprintf(file, "#o:%d|%s|%d|%s|%s|%d|%d|%s|%s|\n",
            (int)object_get_id(game_get_object_at_id(game, i)), object_get_name(game_get_object_at_id(game, i)),
            (int)game_get_object_location(game, object_get_id(game_get_object_at_id(game, i))), object_get_description(game_get_object_at_id(game, i)),
            mobility, (int)object_get_dependecy(game_get_object_at_id(game, i)), (int)object_get_link_id(game_get_object_at_id(game, i)), iluminate, on);
  }
  fprintf(file, "\n");

  /* Se guarda el jugador */

  ids = player_get_objects(game_get_player(game));
  for (int j = 0; j < player_get_num_of_objects(game_get_player(game)); j++)
  {
    if (j == 0)
    {
      strcpy(player_inventory, object_get_name(game_get_object_at_id(game, ids[j])));
      strcat(player_inventory, "|");
    }
    else
    {
      strcat(player_inventory, object_get_name(game_get_object_at_id(game, ids[j])));
      strcat(player_inventory, "|");
    }
  }
  fprintf(file, "#p:%d|%s|%d|%d|%d|%s|\n", (int)player_get_id(game_get_player(game)), player_get_name(game_get_player(game)),
          (int)player_get_location(game_get_player(game)), inventory_get_maxObj(player_get_backpack(game_get_player(game))),
          player_get_num_of_objects(game_get_player(game)), player_inventory);

  fprintf(file, "\n");

  /* Se guardan los links */
  for (i = 1; game_get_link_at_id(game, i) != NULL; i++)
  {

    fprintf(file, "#l:%d|%s|%d|%d|%d|\n", (int)link_get_id(game_get_link_at_id(game, i)), link_get_name(game_get_link_at_id(game, i)),
            (int)link_get_first_id(game_get_link_at_id(game, i)), (int)link_get_second_id(game_get_link_at_id(game, i)), link_get_status(game_get_link_at_id(game, i)));
  }

  return OK;
}

STATUS game_management_load(Game *game, char *filename)
{
  for (int i = 0; i < MAX_SPACES; i++)
  {
    game_free_spaces(game, i);
  }

  for (int i = 0; i < MAX_OBJECTS; i++)
  {
    game_free_objects(game, i);
  }

  game_free_player(game);

  for (int i = 0; i < MAX_LINKS; i++)
  {
    game_free_links(game, i);
  }

  if (game_management_links(game, filename) == ERROR)
  {
    return ERROR;
  }
  if (game_management_spaces(game, filename) == ERROR)
  {
    return ERROR;
  }

  if (game_management_objects(game, filename) == ERROR)
  {
    return ERROR;
  }

  if (game_management_player(game, filename) == ERROR)
  {
    return ERROR;
  }

  return OK;
}
