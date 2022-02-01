/**
 * @brief It defines a textual graphic engine
 *
 * @file graphic_engine.h
 * @author Edited by Iván Fernández París
 * @version 2.0
 * @date 07-02-2021
 * @copyright GNU Public License
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "screen.h"
#include "graphic_engine.h"
#include "gameManagement.h"
#include "command.h"
#include "set.h"
#include "inventory.h"
#include "link.h"

#define STRING_OBJ_SIZE 64 /* !< longitud máxima para el nombre de un objeto leído */
#define MAX_ROW_SIZE 25	   /* !< longitud máxima para el nombre de un objeto leído */

struct _Graphic_engine /*!< Estructura del graphic engine */
{
	Area *map;		/*!< Area de la ventana de las casillas */
	Area *descript; /*!< Area de la ventana de las descripciones */
	Area *banner;	/*!< Area de la ventana del banner */
	Area *help;		/*!< Area de la ventana de ayuda */
	Area *feedback; /*!< Area de la ventana de la salida de los comandos*/
};

Graphic_engine *graphic_engine_create()
{
	static Graphic_engine *ge = NULL;

	if (ge)
		return ge;

	screen_init();
	ge = (Graphic_engine *)malloc(sizeof(Graphic_engine));
	if (ge == NULL)
		return NULL;

	ge->map = screen_area_init(1, 1, 65, 28);
	ge->descript = screen_area_init(68, 1, 45, 28);
	ge->banner = screen_area_init(28, 30, 28, 1);
	ge->help = screen_area_init(1, 31, 112, 3);
	ge->feedback = screen_area_init(1, 35, 112, 3);

	return ge;
}

void graphic_engine_destroy(Graphic_engine *ge)
{
	if (!ge)
		return;

	screen_area_destroy(ge->map);
	screen_area_destroy(ge->descript);
	screen_area_destroy(ge->banner);
	screen_area_destroy(ge->help);
	screen_area_destroy(ge->feedback);

	screen_destroy();
	free(ge);
}

void graphic_engine_paint_game(Graphic_engine *ge, Game *game)
{
	Id id_act = NO_ID, id_back = NO_ID, id_next = NO_ID, *player_inventary = NULL;
	int i = 0, j = 0;
	Space *space_act = NULL;
	char str[255], str_Objaux[STRING_OBJ_SIZE];
	T_Command last_cmd = UNKNOWN;
	extern char *cmd_to_str[N_CMD][N_CMDT];
	BOOL iluminate_flag = FALSE;

	char status[10];
	str_Objaux[0] = '\0';

	/*!< Paint the in the map area */
	screen_area_clear(ge->map);
	if ((id_act = game_get_player_location(game)) != NO_ID)
	{
		int num_objects;
		Id *ids = NULL, link_left = NO_ID, link_right = NO_ID, link_up = NO_ID, link_down = NO_ID;

		space_act = game_get_space(game, id_act);
		id_back = link_get_pair_id(game_get_link_at_id(game, space_get_north(space_act)), id_act);
		id_next = link_get_pair_id(game_get_link_at_id(game, space_get_south(space_act)), id_act);

		if (id_back != NO_ID)
		{

			if (space_get_iluminado(game_get_space(game, id_back)) == TRUE)
			{
				num_objects = space_get_num_objects(game_get_space(game, id_back)); /* !< Recogemos el número de objetos que hay en la casilla */
				ids = space_get_objects(game_get_space(game, id_back));				/* !< Recogemos el id de los objetos que hay en la casilla */
				link_left = link_get_pair_id(game_get_link_at_id(game, space_get_west(game_get_space(game, id_back))), id_back);
				link_right = link_get_pair_id(game_get_link_at_id(game, space_get_east(game_get_space(game, id_back))), id_back);

				/* !< Diferentes posibilidades que tiene la primera fila de la casilla dependiendo de los links que tenga */
				if ((int)space_get_west(game_get_space(game, id_back)) != NO_ID && (int)space_get_east(game_get_space(game, id_back)) != NO_ID)
				{
					sprintf(str, "                  %2d +---------------------------+ %2d", (int)space_get_west(game_get_space(game, id_back)), (int)space_get_east(game_get_space(game, id_back)));
				}
				else if ((int)space_get_west(game_get_space(game, id_back)) != NO_ID && (int)space_get_east(game_get_space(game, id_back)) == NO_ID)
				{
					sprintf(str, "                  %2d +---------------------------+", (int)space_get_west(game_get_space(game, id_back)));
				}
				else if ((int)space_get_west(game_get_space(game, id_back)) == NO_ID && (int)space_get_east(game_get_space(game, id_back)) != NO_ID)
				{
					sprintf(str, "                     +---------------------------+ %2d", (int)space_get_east(game_get_space(game, id_back)));
				}
				else
				{
					sprintf(str, "                     +---------------------------+");
				}

				screen_area_puts(ge->map, str);
				/* !< Diferentes posibilidades que tiene la segunda fila de la casilla dependiendo de los links que tenga y a que casilla se diriga dependiendo de dicho link */
				if (link_left != NO_ID && link_right != NO_ID)
				{
					sprintf(str, "        %s <-- |                        %2d | --> %s", link_get_name(game_get_link_at_id(game, link_left)), (int)id_back, link_get_name(game_get_link_at_id(game, link_right)));
				}
				else if (link_left != NO_ID && link_right == NO_ID)
				{
					sprintf(str, "               %s <-- |                      %2d  |", link_get_name(game_get_link_at_id(game, link_left)), (int)id_back);
				}
				else if (link_left == NO_ID && link_right != NO_ID)
				{
					sprintf(str, "                     |                 %2d  | --> %s", (int)id_back, link_get_name(game_get_link_at_id(game, link_right)));
				}
				else
				{
					sprintf(str, "                     |                       %2d  |", (int)id_back);
				}
				screen_area_puts(ge->map, str);
				sprintf(str, "                     | %s                   |", space_get_gdesc(game_get_space(game, id_back), TOP_LINE));
				screen_area_puts(ge->map, str);
				sprintf(str, "                     | %s                   |", space_get_gdesc(game_get_space(game, id_back), MID_LINE));
				screen_area_puts(ge->map, str);
				sprintf(str, "                     | %s                   |", space_get_gdesc(game_get_space(game, id_back), BOT_LINE));
				screen_area_puts(ge->map, str);

				if (num_objects != 0)
				{
					for (i = 0; i < num_objects; i++)
					{
						if (i == 0) /* !< Si estamos en el primer objecto lo colocamos seguido de la primer pared de la casilla */
						{
							strcpy(str_Objaux, object_get_name(game_get_object_at_id(game, ids[i])));
							strcat(str_Objaux, " ");
						}
						else if (i < (num_objects)) /* !< Concatenamos el resto de objetos */
						{
							if ((strlen(str_Objaux) + strlen(object_get_name(game_get_object_at_id(game, ids[i])))) > MAX_ROW_SIZE)
							{
								j = strlen(str_Objaux);
								while (j <= MAX_ROW_SIZE)
								{
									strcat(str_Objaux, " ");
									j++;
								}
								sprintf(str, "                     | %s|", str_Objaux); /* !< Imprimimos dicha cadena de nombres en la fila correspondiente de la casilla */
								screen_area_puts(ge->map, str);
								strcpy(str_Objaux, object_get_name(game_get_object_at_id(game, ids[i])));
								strcat(str_Objaux, " ");
							}
							else
							{
								strcat(str_Objaux, object_get_name(game_get_object_at_id(game, ids[i])));
								strcat(str_Objaux, " ");
							}
						}
					}

					j = strlen(str_Objaux);
					while (j <= MAX_ROW_SIZE)
					{
						strcat(str_Objaux, " ");
						j++;
					}

					sprintf(str, "                     | %s|", str_Objaux); /* !< Imprimimos dicha cadena de nombres en la fila correspondiente de la casilla */
					screen_area_puts(ge->map, str);
				}
				else
				{
					sprintf(str, "                     |                           |"); /* !< En caso de no haber objetos en la casilla se imprime una fila en blanco delimitada por los bordes de la casilla */
					screen_area_puts(ge->map, str);
				}

				sprintf(str, "                     +---------------------------+");
				screen_area_puts(ge->map, str);
				sprintf(str, "                                ^%s", link_get_name(game_get_link_at_id(game, id_back)));
				screen_area_puts(ge->map, str);
			}
			else
			{
				sprintf(str, "                     +---------------------------+");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                       %2d  |", (int)id_back);
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     +---------------------------+");
				screen_area_puts(ge->map, str);
				sprintf(str, "                                ^%s", link_get_name(game_get_link_at_id(game, id_back)));
				screen_area_puts(ge->map, str);
			}
		}

		if (id_act != NO_ID)
		{

			/* Comprobamos si el jugador tiene un objeto que ilumine espacios */
			player_inventary = player_get_objects(game_get_player(game));
			if (player_get_num_of_objects(game_get_player(game)) != 0) /* !< Si el jugador tiene al menos 1 objeto en su inventario imprimimos el nombre de dicho objeto */
			{
				for (i = 0; i < player_get_num_of_objects(game_get_player(game)); i++)
				{
					if (object_is_turnedon(game_get_object_at_id(game, player_inventary[i])) == TRUE)
					{
						iluminate_flag = TRUE;
					}
				}
			}

			if (space_get_iluminado(space_act) == TRUE || iluminate_flag == TRUE)
			{
				num_objects = space_get_num_objects(space_act);
				ids = space_get_objects(space_act);
				link_left = link_get_pair_id(game_get_link_at_id(game, space_get_west(space_act)), id_act);
				link_right = link_get_pair_id(game_get_link_at_id(game, space_get_east(space_act)), id_act);
				link_up = link_get_pair_id(game_get_link_at_id(game, space_get_up(game_get_space(game, id_act))), id_act);
				link_down = link_get_pair_id(game_get_link_at_id(game, space_get_down(game_get_space(game, id_act))), id_act);

				if (space_get_west(space_act) != NO_ID && space_get_east(space_act) != NO_ID)
				{
					sprintf(str, "                  %2d +---------------------------+ %2d", (int)space_get_west(space_act), (int)space_get_east(space_act));
				}
				else if (space_get_west(space_act) != NO_ID && space_get_east(space_act) == NO_ID)
				{
					sprintf(str, "                  %2d +---------------------------+", (int)space_get_west(space_act));
				}
				else if (space_get_west(space_act) == NO_ID && space_get_east(space_act) != NO_ID)
				{
					sprintf(str, "                     +---------------------------+ %2d", (int)space_get_east(space_act));
				}
				else
				{
					sprintf(str, "                     +---------------------------+");
				}
				screen_area_puts(ge->map, str);
				if (link_left != NO_ID && link_right != NO_ID)
				{
					sprintf(str, "         %s <-- |   %s               %2d | --> %s", link_get_name(game_get_link_at_id(game, link_left)), player_get_name(game_get_player(game)), (int)id_act, link_get_name(game_get_link_at_id(game, link_right)));
				}
				else if (link_up != NO_ID && link_right != NO_ID)
				{
					sprintf(str, "        %s ^^^ |                        %2d | --> %s", link_get_name(game_get_link_at_id(game, link_up)), (int)id_act, link_get_name(game_get_link_at_id(game, link_right)));
				}
				else if (link_left != NO_ID && link_down != NO_ID)
				{
					sprintf(str, "        %s <-- |                        %2d | vvv %s", link_get_name(game_get_link_at_id(game, link_left)), (int)id_act, link_get_name(game_get_link_at_id(game, link_down)));
				}
				else if (link_left != NO_ID && link_right == NO_ID)
				{
					sprintf(str, "     %s <-- |     %s                 %2d  |", link_get_name(game_get_link_at_id(game, link_left)), player_get_name(game_get_player(game)), (int)id_act);
				}
				else if (link_left == NO_ID && link_right != NO_ID)
				{
					sprintf(str, "                 |    %s             %2d  | --> %s", player_get_name(game_get_player(game)), (int)id_act, link_get_name(game_get_link_at_id(game, link_right)));
				}
				else
				{
					sprintf(str, "                     |  %s                 %2d  |", player_get_name(game_get_player(game)), (int)id_act);
				}
				screen_area_puts(ge->map, str);
				sprintf(str, "                     | %s                   |", space_get_gdesc(game_get_space(game, id_act), TOP_LINE));
				screen_area_puts(ge->map, str);
				sprintf(str, "                     | %s                   |", space_get_gdesc(game_get_space(game, id_act), MID_LINE));
				screen_area_puts(ge->map, str);
				sprintf(str, "                     | %s                   |", space_get_gdesc(game_get_space(game, id_act), BOT_LINE));
				screen_area_puts(ge->map, str);

				if (num_objects != 0)
				{
					for (i = 0; i < num_objects; i++)
					{
						if (i == 0) /* !< Si estamos en el primer objecto lo colocamos seguido de la primer pared de la casilla */
						{
							strcpy(str_Objaux, object_get_name(game_get_object_at_id(game, ids[i])));
							strcat(str_Objaux, " ");
						}
						else if (i < (num_objects)) /* !< Concatenamos el resto de objetos */
						{
							/* Antes de concatenar el objeto, comprobamos que si se añade no supere la longitud máxima permitida por fila */
							if ((strlen(str_Objaux) + strlen(object_get_name(game_get_object_at_id(game, ids[i])))) > MAX_ROW_SIZE) /* En caso de superar la longitud máxima... */
							{
								j = strlen(str_Objaux);
								while (j <= MAX_ROW_SIZE) /* Rellenamos los huecos restantes hasta llegar al máximo */
								{
									strcat(str_Objaux, " ");
									j++;
								}
								sprintf(str, "                     | %s|", str_Objaux); /* Imprimimos dicha cadena de nombres en la fila correspondiente de la casilla */
								screen_area_puts(ge->map, str);
								strcpy(str_Objaux, object_get_name(game_get_object_at_id(game, ids[i]))); /* Y copiamos el nombre del objeto que se queda fuera de la fila en la cadena auxiliar, que esta se imprimirá en la siguiente fila */
								strcat(str_Objaux, " ");
							}
							else
							{ /* Si no supera el máximo permitido, se concatena */
								strcat(str_Objaux, object_get_name(game_get_object_at_id(game, ids[i])));
								strcat(str_Objaux, " ");
							}
						}
					}

					j = strlen(str_Objaux);
					while (j <= MAX_ROW_SIZE)
					{
						strcat(str_Objaux, " ");
						j++;
					}

					sprintf(str, "                     | %s|", str_Objaux); /* !< Imprimimos dicha cadena de nombres en la fila correspondiente de la casilla */
					screen_area_puts(ge->map, str);
				}
				else
				{
					sprintf(str, "                     |                           |");
					screen_area_puts(ge->map, str);
				}

				sprintf(str, "                     +---------------------------+");
				screen_area_puts(ge->map, str);
			}
			else
			{
				sprintf(str, "                     +---------------------------+");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     | %s                  %2d  |", player_get_name(game_get_player(game)), (int)id_act);
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     +---------------------------+");
				screen_area_puts(ge->map, str);
			}
		}

		if (id_next != NO_ID)
		{
			if (space_get_iluminado(game_get_space(game, id_next)) == TRUE)
			{

				num_objects = space_get_num_objects(game_get_space(game, id_next));
				ids = space_get_objects(game_get_space(game, id_next));
				link_left = link_get_pair_id(game_get_link_at_id(game, space_get_west(game_get_space(game, id_next))), id_next);
				link_right = link_get_pair_id(game_get_link_at_id(game, space_get_east(game_get_space(game, id_next))), id_next);

				sprintf(str, "                                ^%s", link_get_name(game_get_link_at_id(game, id_act)));
				screen_area_puts(ge->map, str);
				if (space_get_west(game_get_space(game, id_next)) != NO_ID && space_get_east(game_get_space(game, id_next)) != NO_ID)
				{
					sprintf(str, "                  %2d +---------------------------+ %2d", (int)space_get_west(game_get_space(game, id_next)), (int)space_get_east(game_get_space(game, id_next)));
				}
				else if (space_get_west(game_get_space(game, id_next)) != NO_ID && space_get_east(game_get_space(game, id_next)) == NO_ID)
				{
					sprintf(str, "                  %2d +---------------------------+", (int)space_get_west(game_get_space(game, id_next)));
				}
				else if (space_get_west(game_get_space(game, id_next)) == NO_ID && space_get_east(game_get_space(game, id_next)) != NO_ID)
				{
					sprintf(str, "                      +----------------------------+ %2d", (int)space_get_east(game_get_space(game, id_next)));
				}
				else
				{
					sprintf(str, "                     +---------------------------+");
				}
				screen_area_puts(ge->map, str);
				if (link_left != NO_ID && link_right != NO_ID)
				{
					sprintf(str, "        %s <-- |                        %2d | --> %s", link_get_name(game_get_link_at_id(game, link_left)), (int)id_next, link_get_name(game_get_link_at_id(game, link_right)));
				}
				else if (link_left != NO_ID && link_right == NO_ID)
				{
					sprintf(str, "              %s <-- |                      %2d  |", link_get_name(game_get_link_at_id(game, link_left)), (int)id_next);
				}
				else if (link_left == NO_ID && link_right != NO_ID)
				{
					sprintf(str, "                     |                 %2d  | --> %s", (int)id_next, link_get_name(game_get_link_at_id(game, link_right)));
				}
				else
				{
					sprintf(str, "                     |                       %2d  |", (int)id_next);
				}
				screen_area_puts(ge->map, str);
				sprintf(str, "                     | %s                   |", space_get_gdesc(game_get_space(game, id_next), TOP_LINE));
				screen_area_puts(ge->map, str);
				sprintf(str, "                     | %s                   |", space_get_gdesc(game_get_space(game, id_next), MID_LINE));
				screen_area_puts(ge->map, str);
				sprintf(str, "                     | %s                   |", space_get_gdesc(game_get_space(game, id_next), BOT_LINE));
				screen_area_puts(ge->map, str);

				if (num_objects != 0)
				{
					for (i = 0; i < num_objects; i++)
					{
						if (i == 0) /* !< Si estamos en el primer objecto lo colocamos seguido de la primer pared de la casilla */
						{
							strcpy(str_Objaux, object_get_name(game_get_object_at_id(game, ids[i])));
							strcat(str_Objaux, " ");
						}
						else if (i < (num_objects)) /* !< Concatenamos el resto de objetos */
						{
							if ((strlen(str_Objaux) + strlen(object_get_name(game_get_object_at_id(game, ids[i])))) > MAX_ROW_SIZE)
							{
								j = strlen(str_Objaux);
								while (j <= MAX_ROW_SIZE)
								{
									strcat(str_Objaux, " ");
									j++;
								}
								sprintf(str, "                     | %s|", str_Objaux); /* !< Imprimimos dicha cadena de nombres en la fila correspondiente de la casilla */
								screen_area_puts(ge->map, str);
								strcpy(str_Objaux, object_get_name(game_get_object_at_id(game, ids[i])));
								strcat(str_Objaux, " ");
							}
							else
							{
								strcat(str_Objaux, object_get_name(game_get_object_at_id(game, ids[i])));
								strcat(str_Objaux, " ");
							}
						}
					}

					j = strlen(str_Objaux);
					while (j <= MAX_ROW_SIZE)
					{
						strcat(str_Objaux, " ");
						j++;
					}

					sprintf(str, "                     | %s|", str_Objaux); /* !< Imprimimos dicha cadena de nombres en la fila correspondiente de la casilla */
					screen_area_puts(ge->map, str);
				}
				else
				{
					sprintf(str, "                     |                           |");
					screen_area_puts(ge->map, str);
				}

				sprintf(str, "                     +---------------------------+");
				screen_area_puts(ge->map, str);
			}
			else
			{
				sprintf(str, "                                ^%s", link_get_name(game_get_link_at_id(game, id_act)));
				screen_area_puts(ge->map, str);
				sprintf(str, "                     +---------------------------+");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                       %2d  |", (int)id_next);
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     |                           |");
				screen_area_puts(ge->map, str);
				sprintf(str, "                     +---------------------------+");
				screen_area_puts(ge->map, str);
			}
		}
	}

	/* !< Paint in the description area */
	char auxString[STRING_OBJ_SIZE];

	screen_area_clear(ge->descript);
	sprintf(str, " Objects location: ");
	screen_area_puts(ge->descript, str);
	for (i = 0; i < (MAX_OBJECTS + 1); i++)
	{
		if (game_get_object_location(game, i) != NO_ID)
		{
			sprintf(str, "  %s:%d", object_get_name(game_get_object_at_id(game, i)), (int)game_get_object_location(game, i));
			screen_area_puts(ge->descript, str);
		}
	}
	sprintf(str, " ");
	screen_area_puts(ge->descript, str);

	player_inventary = player_get_objects(game_get_player(game));
	if (player_get_num_of_objects(game_get_player(game)) != 0) /* !< Si el jugador tiene al menos 1 objeto en su inventario imprimimos el nombre de dicho objeto */
	{
		for (i = 0; i < player_get_num_of_objects(game_get_player(game)); i++)
		{
			if (i == 0)
			{
				strcpy(auxString, object_get_name(game_get_object_at_id(game, player_inventary[i])));
			}
			else
			{
				strcat(auxString, ", ");
				strcat(auxString, object_get_name(game_get_object_at_id(game, player_inventary[i])));
			}
		}
		sprintf(str, " Player object: %s", auxString);
		screen_area_puts(ge->descript, str);
	}
	else /* !< En caso de que el jugador no tenga objetos en su inventario */
	{
		sprintf(str, " the player has no object");
		screen_area_puts(ge->descript, str);
	}
	sprintf(str, " ");
	screen_area_puts(ge->descript, str);

	/* Impresion del dado */
	/*if (game_get_last_roll(game) != 0)
	{
		sprintf(str, " Last die value: %ld", game_get_last_roll(game));
	}
	else
	{
		sprintf(str, " Last die value: the die       hasn't been rolled");
	}
	screen_area_puts(ge->descript, str);*/

	/* !< Paint the description information */
	sprintf(str, " Descriptions: ");
	screen_area_puts(ge->descript, str);

	/* !< En caso de ejecutar el comando INSPECT y que dicho comando se haya ejecutado correctamente se imprime la descripcion del objeto o de la casilla indicada
	(la salida será correcta si el espacio tiene descripcion o el objeto está en el inventario o en la casilla donde se encuentra el jugador) */
	if (game_get_last_command(game) == INSPECT && (iluminate_flag == TRUE || space_get_iluminado(game_get_space(game, id_act)) == TRUE) && game_get_last_cmd_status(game) == OK)
	{
		sprintf(str, "  %s", game_get_lastDescription(game));
		screen_area_puts(ge->descript, str);
	}
	else
	{
		sprintf(str, "  Space description-%s", space_get_description(game_get_space(game, game_get_player_location(game))));
		screen_area_puts(ge->descript, str);
	}

	/* !< Paint in the banner area */
	screen_area_puts(ge->banner, " LA TUMBA DEL REY ");

	/* !< Paint in the help area */
	screen_area_clear(ge->help);
	sprintf(str, " The commands you can use are:");
	screen_area_puts(ge->help, str);
	sprintf(str, "     take or t, drop or d, roll o rl, move or m, inspect or i, turnon or on, turnoff or off, open or o, save or s, load or l, exit or e");
	screen_area_puts(ge->help, str);

	/* !< Paint in the feedback area */
	if (game_get_last_cmd_status(game) == OK)
	{
		sprintf(status, "OK");
	}
	else
	{
		sprintf(status, "ERROR");
	}
	last_cmd = game_get_last_command(game);

	//sprintf(str, " %s (%s): %s", cmd_to_str[last_cmd - NO_CMD][CMDL], cmd_to_str[last_cmd - NO_CMD][CMDS], status);
	//screen_area_puts(ge->feedback, str);

	strcpy(str, dialogue_get_explicative_phrase(game, cmd_to_str[last_cmd - NO_CMD][CMDL], status));
	screen_area_puts(ge->feedback, str);
	/* !< Dump to the terminal */
	screen_paint();
	printf("prompt:> ");
}
