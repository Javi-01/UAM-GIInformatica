/**
 * @brief It defines the game loop
 *
 * @file game_loop.c
 * @author Profesores PPROG
 * @version 2.0
 * @date 13-01-2020
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "graphic_engine.h"
#include "game.h"
#include "player.h"
#include "command.h"

int game_loop_init(Game **game, Graphic_engine **gengine, char *file_name);
void game_loop_run(Game *game, Graphic_engine *gengine, char *logFileName);
void game_loop_cleanup(Game *game, Graphic_engine *gengine);

/**
* @brief Función main que ejecuta todo el juego.
* @param argc número de argumentos introducidos por terminal 
* @param argv argumentos introducidos
* @return 0 si no ocurrió ningún tipo de error o 1 en caso contrario
*
* @author Modificada por Iván Fernández París
*/
int main(int argc, char *argv[])
{

	Game *game;
	Graphic_engine *gengine;
	char *logFileName = NULL;

	if (argc < 2)
	{
		fprintf(stderr, "Use: %s <game_data_file>\n", argv[0]);
		return 1;
	}
	if (argc >= 4) //Caso logFile
	{
		if (!strcmp("-l", argv[2]))
		{
			logFileName = argv[3];
		}
		else
		{
			fprintf(stderr, "The option: %s not available.\n Write -l (followed by the log file name) if you want to create a log file or nothing if you don't want to create it.\n Si  \n", argv[2]);
			return 1;
		}
	}

	if (!game_loop_init(&game, &gengine, argv[1]))
	{
		game_loop_run(game, gengine, logFileName);
		game_loop_cleanup(game, gengine);
	}

	return 0;
}
/**
* @brief Crea un juego 
*
* @param game doble puntero a game 
* @param gengine doble puntero a graphic_engine
* @param file_name nombre del fichero que servirá para cargar los datos del juego (data.dat)
* @return 0 si no ocurrió ningún tipo de error o 1 en caso contrario 
*
* @author Modificada por Iván Fernández París
*/
int game_loop_init(Game **game, Graphic_engine **gengine, char *file_name)
{

	if ((*game = game_create()) == NULL)
	{
		fprintf(stderr, "Error while initializing game.\n");
		game_destroy(*game);
		return 1;
	}

	if (game_create_from_file(*game, file_name) == ERROR)
	{
		fprintf(stderr, "Error while initializing game.\n");
		return 1;
	}

	if ((*gengine = graphic_engine_create()) == NULL)
	{
		fprintf(stderr, "Error while initializing graphic engine.\n");
		game_destroy(*game);
		return 1;
	}

	return 0;
}
/**
* @brief Ejecuta cada una de las iteraciones del juego
*
* @param game puntero a game 
* @param gengine puntero a graphic_engine
* @param logFileName nombre del fichero donde será almacenada la informaciñon acerca de la ejecución de los comandos
*
* @author Modificada por Iván Fernández París
*/
void game_loop_run(Game *game, Graphic_engine *gengine, char *logFileName)
{

	T_Command command = NO_CMD;
	FILE *logFile = NULL;
	extern char *cmd_to_str[N_CMD][N_CMDT];

	if (logFileName != NULL)
	{
		logFile = fopen(logFileName, "w");
	}

	while ((command != EXIT) && !game_get_finished(game))
	{
		graphic_engine_paint_game(gengine, game);
		command = command_get_user_input();
		game_update(game, command);

		if (logFileName != NULL)
		{
			if (game_get_last_cmd_status(game) == OK && command != NO_CMD)
			{
				if (command == TAKE)
				{
					fprintf(logFile, " %s (%s) %s: OK\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS], object_get_name(game_get_object_at_id(game, player_get_last_object(game_get_player(game)))));
				}
				else if (command == DROP)
				{
					fprintf(logFile, " %s (%s) %s: OK\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS], object_get_name(game_get_object_at_id(game, player_get_last_object(game_get_player(game)))));
				}
				else if (command == MOVE)
				{
					fprintf(logFile, " %s (%s) %s: OK\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS], game_get_last_move(game));
				}
				else
				{
					fprintf(logFile, " %s (%s): OK\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
				}
			}
			else if (game_get_last_cmd_status(game) == ERROR && command != NO_CMD)
			{
				if (command == TAKE)
				{
					fprintf(logFile, " %s (%s) %s: ERROR\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS], object_get_name(game_get_object_at_id(game, player_get_last_object(game_get_player(game)))));
				}
				else if (command == DROP)
				{
					fprintf(logFile, " %s (%s) %s: ERROR\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS], object_get_name(game_get_object_at_id(game, player_get_last_object(game_get_player(game)))));
				}
				else if (command == MOVE)
				{
					fprintf(logFile, " %s (%s) %s: ERROR\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS], game_get_last_move(game));
				}
				else
				{
					fprintf(logFile, " %s (%s): ERROR\n", cmd_to_str[command - NO_CMD][CMDL], cmd_to_str[command - NO_CMD][CMDS]);
				}
			}
		}
	}
	if (logFileName != NULL)
	{
		fclose(logFile);
	}
}

/**
* @brief Destruye game y graphic engine creando memoria
*
* @param game puntero a game 
* @param gengine puntero a graphic_engine
*
* @author Profesores PPROG
*/
void game_loop_cleanup(Game *game, Graphic_engine *gengine)
{

	game_destroy(game);
	graphic_engine_destroy(gengine);
}
