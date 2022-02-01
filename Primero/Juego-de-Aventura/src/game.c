/** 
 * @brief It implements the game interface and all the associated callbacks
 * for each command
 * 
 * @file game.c
 * @author Modificado Javier Fraile Iglesias e Iván Fernández París y José Miguel Nicolás García
 * @version 2.0 
 * @date 13-01-2015 
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "game.h"
#include "gameManagement.h"
#include "player.h"
#include "object.h"
#include "die.h"
#include "set.h"
#include "inventory.h"
#include "link.h"
#include "dialogue.h"

#define N_CALLBACK 13 /*!< Número de comandos que se pueden hacer */
#define PLAYER_ID 1	  /*!< Id de player  */
#define OBJECT_ID 1	  /*!< Id del objeto */
#define DIE_ID 1	  /*!< Id del dado */
#define DIE_MAX 6	  /*!< Id del dado */
#define DIE_MIN 1	  /*!< Id del dado */
#define MAX_LEN 128	  /*!< Longitud máxima del nombre del objeto */

/**
   Define the function type for the callbacks
*/
typedef STATUS (*callback_fn)(Game *game);

/**
   List of callbacks for each command in the game 
*/

/**
* @brief Función que se llama cuando la callback es incorrecta.
* @param game puntero a game 
* @return Ok si se ejecuta dicho comando
*
* @author Profesores PPROG
*/
STATUS game_callback_unknown(Game *game);

/**
* @brief Función utilizada para salir del juego.
* @param game puntero a game 
* @return Ok si se ejecuta dicho comando
*
* @author Profesores PPROG
*/
STATUS game_callback_exit(Game *game);

/**
* @brief Hace qeue el jugador coja el objeto que se encuentre en la misma casilla que él.
* @param game puntero a game 
* @return Ok en caso de salir todo bien o ERROR en caso contario
*
* @author Modificada por Iván Fernández París
*/
STATUS game_callback_take(Game *game);

/**
* @brief Hace que el jugador deje un objeto previamente cogido.
* @param game puntero a game 
* @return Ok en caso de salir todo bien o ERROR en caso contario
*
* @author Modificada por Iván Fernández París
*/
STATUS game_callback_drop(Game *game);

/**
* @brief Lanza el dado.
* @param game puntero a game 
* @return Ok en caso de salir todo bien o ERROR en caso contario
*
* @author José Miguel Nicolás García
*/
STATUS game_callback_roll(Game *game);

/**
* @brief Comando para moverse entre espacios enlazados
* @param game puntero a game 
* @return Ok en caso de salir todo bien o ERROR en caso contario
*
* @author Javier Fraile Iglesias
*/
STATUS game_callback_move(Game *game);

/**
* @brief Comando para inspeccionar la descripción de espacios y objetos
* @param game puntero a game 
* @return Ok en caso de salir todo bien o ERROR en caso contario
*
* @author José Miguel Nicolás García
*/
STATUS game_callback_inspect(Game *game);

/**
* @brief Comando para encender objetos
* @param game puntero a game 
* @return Ok en caso de salir todo bien o ERROR en caso contario
*
* @author Iván Fernández París
*/
STATUS game_callback_turnon(Game *game);

/**
* @brief Comando para apagar objetos
* @param game puntero a game 
* @return Ok en caso de salir todo bien o ERROR en caso contario
*
* @author Iván Fernández París
*/
STATUS game_callback_turnoff(Game *game);

/**
* @brief Comando para abrir links
* @param game puntero a game 
* @return Ok en caso de salir todo bien o ERROR en caso contario
*
* @author Iván Fernández París
*/
STATUS game_callback_open(Game *game);

/**
* @brief Comando para guardar partida
* @param game puntero a game 
* @return Ok en caso de salir todo bien o ERROR en caso contario
*
* @author José Miguel Nicolás García
*/
STATUS game_callback_save(Game *game);

/**
* @brief Comando para cargar partida guardada
* @param game puntero a game 
* @return Ok en caso de salir todo bien o ERROR en caso contario
*
* @author José Miguel Nicolás García
*/
STATUS game_callback_load(Game *game);

static callback_fn game_callback_fn_list[N_CALLBACK] = {
	game_callback_unknown,
	game_callback_exit,
	game_callback_roll,
	game_callback_take,
	game_callback_drop,
	game_callback_move,
	game_callback_inspect,
	game_callback_turnon,
	game_callback_turnoff,
	game_callback_open,
	game_callback_save,
	game_callback_load,
};

/**
   !< Private functions
*/

/**
* @brief Proporciona el id de una posición indicada
* @param game puntero a game 
* @param position posicion del space del cual se quiere saber su Id
* @return Devuelve el id de dicha posición.
*
* @author Profesores PPROG
*/
Id game_get_space_id_at(Game *game, int position);

/**
* @brief Cambia la posición del jugador.
* @param game puntero a game 
* @param id id del jugador al que se establecerá su posición
* @return Devuelve el id de dicha posición.

* @author Profesores PPROG
*/
STATUS game_set_player_location(Game *game, Id id);

/**
* @brief Cambia la dirección del último movimiento realizado.
* @param game puntero a game 
* @param move cadena con el nombre de la dirección
* @return Ok si se copio correctamente o ERROR en caso contrario

* @author Iván Fernández París
*/
STATUS game_set_last_move(Game *game, char *move);

/**
* @brief Cambia el estado del game
* @param game puntero a game 
* @param finished indica si el juego a finalizado

* @author Iván Fernández París
*/
void game_set_finished(Game *game, BOOL finished);

/**
   !< Game interface implementation
*/

struct _Game /*!< Estrucutura de game */
{
	Space *spaces[MAX_SPACES + 1];		 /*!< Puntero a la estructura space */
	Object *object[MAX_OBJECTS + 1];	 /*!< Puntero a la estructura object */
	Player *player;						 /*!< Puntero a la estructura player */
	Die *die;							 /*!< Puntero a la estructura del dado */
	T_Command last_cmd;					 /*!< Puntero a la estructura command */
	STATUS last_cmd_status;				 /*!< Status del último comando ejecutado */
	Link *link[MAX_LINKS + 1];			 /*!< Puntero a la estructura link */
	char last_move[MAX_LEN];			 /*!< cadena con la ultima direccion del movimiento */
	char lastDescription[WORD_SIZE + 1]; /*!< cadena con la ultima descripcion inspeccionada */
	BOOL game_finished;					 /*!< Indica si el juego ha finalizado*/
	Dialogue *dialogue;					 /*!< Diálogos del juego*/
};

Game *game_create()
{

	Game *game;
	int i;

	game = (Game *)malloc(sizeof(Game));
	if (!game)
	{
		return NULL;
	}

	for (i = 0; i < MAX_SPACES; i++)
	{
		game->spaces[i] = NULL;
	}

	for (i = 0; i < MAX_OBJECTS; i++)
	{
		game->object[i] = NULL;
	}

	for (i = 0; i < MAX_LINKS; i++)
	{
		game->link[i] = NULL;
	}

	game->player = NULL;
	game->die = die_create(DIE_ID, DIE_MAX, DIE_MIN);
	game->dialogue = dialogue_create();

	game->last_cmd = NO_CMD;
	game->last_cmd_status = OK;
	*game->last_move = '\0';
	game->game_finished = FALSE;

	return game;
}

STATUS game_create_from_file(Game *game, char *filename)
{

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

STATUS game_destroy(Game *game)
{

	int i = 0;

	for (i = 0; (i < MAX_SPACES) && (game->spaces[i] != NULL); i++)
	{
		space_destroy(game->spaces[i]);
	}
	for (i = 0; (i < MAX_LINKS) && (game->link[i] != NULL); i++)
	{
		link_destroy(game->link[i]);
	}

	player_destroy(game->player);
	for (i = 0; (i < MAX_OBJECTS) && (game->object[i] != NULL); i++)
	{
		object_destroy(game->object[i]);
	}
	die_destroy(game->die);
	dialogue_destroy(game->dialogue);
	free(game);

	return OK;
}

STATUS game_free_objects(Game *game, Id pos)
{
	if (object_destroy(game->object[pos]) == ERROR)
	{
		return ERROR;
	}
	game->object[pos] = NULL;
	return OK;
}
STATUS game_free_links(Game *game, Id pos)
{
	if (link_destroy(game->link[pos]) == ERROR)
	{
		return ERROR;
	}
	game->link[pos] = NULL;
	return OK;
}
STATUS game_free_spaces(Game *game, Id pos)
{
	if (space_destroy(game->spaces[pos]) == ERROR)
	{
		return ERROR;
	}
	game->spaces[pos] = NULL;
	return OK;
}

STATUS game_free_player(Game *game)
{
	if (player_destroy(game->player) == ERROR)
	{
		return ERROR;
	}
	game->player = NULL;
	return OK;
}

Id game_get_space_id_at(Game *game, int position)
{

	if (position < 0 || position >= MAX_SPACES)
	{
		return NO_ID;
	}

	return space_get_id(game->spaces[position]);
}

Id game_get_object_id(Game *game, int posicion)
{
	if (!game)
	{
		return NO_ID;
	}

	return object_get_id(game->object[posicion]);
}

Space *game_get_space(Game *game, Id id)
{

	int i = 0;

	if (id == NO_ID)
	{
		return NULL;
	}

	for (i = 0; i < MAX_SPACES && game->spaces[i] != NULL; i++)
	{
		if (id == space_get_id(game->spaces[i]))
		{
			return game->spaces[i];
		}
	}

	return NULL;
}

Player *game_get_player(Game *game)
{

	if (game == NULL)
		return NULL;

	return game->player;
}

long game_get_last_roll(Game *game)
{

	if (game == NULL)
		return -1;

	return die_get_num(game->die);
}

const char *game_get_last_move(Game *game)
{

	if (game == NULL)
		return NULL;

	return game->last_move;
}

char *game_get_lastDescription(Game *game)
{

	if (game == NULL)
		return NULL;

	return game->lastDescription;
}

STATUS game_set_last_move(Game *game, char *move)
{
	if (game == NULL || !move)
		return ERROR;

	strcpy(game->last_move, move);

	return OK;
}

STATUS game_set_player_location(Game *game, Id id)
{

	if (id == NO_ID)
	{
		return ERROR;
	}

	return player_set_location(game->player, id);
}

Id game_get_player_location(Game *game)
{

	return player_get_location(game->player);
}

Id game_get_object_location(Game *game, Id id)
{
	int i;

	for (i = 0; i < MAX_SPACES && game->spaces[i] != NULL; i++)
	{

		if (space_object_presence(game->spaces[i], id) == TRUE)
		{
			return space_get_id(game->spaces[i]);
		}
	}

	return NO_ID;
}

Link *game_get_link_at_id(Game *game, Id id)
{
	int i;

	for (i = 0; i < MAX_LINKS && game->link[i] != NULL; i++)
	{
		if (link_get_id(game->link[i]) == id)
		{
			return game->link[i];
		}
	}

	return NULL;
}

Object *game_get_object_at_name(Game *game, char *object_name)
{
	int i;

	if (!object_name || !game)
	{
		return NULL;
	}

	for (i = 0; i < MAX_OBJECTS; i++)
	{
		if (strcmp(object_name, object_get_name(game_get_object_at_id(game, game_get_object_id(game, i)))) == 0)
		{
			return game->object[i];
		}
	}

	return NULL;
}

Object *game_get_object_at_id(Game *game, Id id)
{
	int i;

	if (id == NO_ID || !game)
	{
		return NULL;
	}

	for (i = 0; i < MAX_OBJECTS; i++)
	{
		if (object_get_id(game->object[i]) == id)
		{
			return game->object[i];
		}
	}

	return NULL;
}

Object *game_get_object_at_position(Game *game, int position)
{
	int i;

	if (!game || position > (MAX_OBJECTS + 1))
	{
		return NULL;
	}

	for (i = 0; i < MAX_OBJECTS; i++)
	{
		if (i == position)
		{
			return game->object[i];
		}
	}

	return NULL;
}

Link *game_get_link_at_name(Game *game, char *link_name)
{
	int i;

	if (!link_name || !game)
	{
		return NULL;
	}

	for (i = 0; i < MAX_LINKS; i++)
	{
		if (strcmp(link_name, link_get_name(game->link[i])) == 0)
		{
			return game->link[i];
		}
	}

	return NULL;
}

Dialogue *game_get_dialogue(Game *game)
{
	if (!game)
	{
		return ERROR;
	}

	return game->dialogue;
}
STATUS game_update(Game *game, T_Command cmd)
{

	game->last_cmd = cmd;
	game->last_cmd_status = (*game_callback_fn_list[cmd])(game);
	return OK;
}

T_Command game_get_last_command(Game *game)
{

	return game->last_cmd;
}

STATUS game_get_last_cmd_status(Game *game)
{
	return game->last_cmd_status;
}

STATUS game_add_space(Game *game, Space *space)
{

	int i = 0;

	if (space == NULL)
	{
		return ERROR;
	}

	while ((i < MAX_SPACES) && (game->spaces[i] != NULL))
	{
		i++;
	}

	if (i >= MAX_SPACES)
	{
		return ERROR;
	}

	game->spaces[i] = space;

	return OK;
}

STATUS game_add_object(Game *game, Object *object)
{
	int i = 0;

	if (object == NULL)
	{
		return ERROR;
	}

	while ((i < MAX_OBJECTS) && (game->object[i] != NULL))
	{
		i++;
	}

	if (i >= MAX_OBJECTS)
	{
		return ERROR;
	}

	game->object[i] = object;

	return OK;
}

STATUS game_add_player(Game *game, Player *player)
{

	if (player == NULL)
	{
		return ERROR;
	}

	game->player = player;

	return OK;
}

STATUS game_add_link(Game *game, Link *link)
{

	int i = 0;

	if (link == NULL)
	{
		return ERROR;
	}

	while ((i < MAX_LINKS) && (game->link[i] != NULL))
	{
		i++;
	}

	if (i >= MAX_LINKS)
	{
		return ERROR;
	}

	game->link[i] = link;

	return OK;
}

void game_print_data(Game *game)
{

	int i = 0;

	printf("\n\n-------------\n\n");

	printf("=> Spaces: \n");
	for (i = 0; i < MAX_SPACES && game->spaces[i] != NULL; i++)
	{
		space_print(game->spaces[i]);
	}

	player_print(game->player);
	for (i = 0; i < MAX_OBJECTS; i++)
	{
		object_print(game->object[i]);
	}
	printf("prompt:> ");
}

BOOL game_is_over(Game *game)
{

	return FALSE;
}

void game_set_finished(Game *game, BOOL finished)
{
	if (!game)
	{
		return;
	}

	game->game_finished = finished;
}

BOOL game_get_finished(Game *game)
{
	if (!game)
	{
		return FALSE;
	}

	return game->game_finished;
}

void game_manage_final(Game *game)
{
	int i = 0;
	char win_objects[NUM_WIN_OBJECTS][MAX_LEN] = {"medallon", "espada", "escudo", "biblia", "diamante", "grial", "yelmo"}; /* Cadena con los nombres de las ofrendas para ganar */

	if (!game)
	{

		return;
	}

	/* Si el player se encuentra en la casilla 7 (agujero) muere */
	if (game_get_player_location(game) == DEAD_SPACE_1)
	{
		printf("\nHas perdido... \nCaer a un abismo desde gran altura siempre es mortal...\n");
		game_set_finished(game, TRUE);
		return;
	}

	/* Si el player se encuentra en la casilla 12 (camara de gas) sin tener la mascarilla muere */
	if (game_get_player_location(game) == DEAD_SPACE_2 && !player_has_object(game->player, object_get_id(game_get_object_at_name(game, "mascarilla"))))
	{
		printf("\nHas perdido... \nA veces entrar en una máscara de gas sin mascarilla puede ser mortal...\n");
		game_set_finished(game, TRUE);
		return;
	}

	if (game_get_player_location(game) == WIN_SPACE)
	{
		for (int j = 0; j < NUM_WIN_OBJECTS; j++)
		{
			if (space_have_id(game_get_space(game, game_get_player_location(game)), object_get_id(game_get_object_at_name(game, win_objects[j]))))
			{
				i++;
			}
		}

		if (i == NUM_WIN_OBJECTS)
		{
			printf("\nFelicidades, has superado el juego... \nTodas las ofrendas fueron llevadas a la tumba\n");
			game_set_finished(game, TRUE);
			return;
		}
	}

	return;
}

/**
   !< Callbacks implementation for each action 
*/

STATUS game_callback_unknown(Game *game)
{
	return OK;
}

STATUS game_callback_exit(Game *game)
{
	return OK;
}

STATUS game_callback_take(Game *game)
{
	Space *playerSpace = NULL;
	char object_name[MAX_LEN];

	scanf("%s", object_name);
	object_name[strlen(object_name) + 1] = '\0';

	playerSpace = game_get_space(game, game_get_player_location(game));

	if (space_get_id(playerSpace) == game_get_player_location(game) && game_get_player_location(game) == game_get_object_location(game, object_get_id(game_get_object_at_name(game, object_name))))
	{
		if (player_can_take_an_object(game->player, game_get_object_at_name(game, object_name)))
		{
			/* Comprobamos si el judaor lo puede meter en su mochila */
			if (player_set_object(game->player, object_get_id(game_get_object_at_name(game, object_name))) == ERROR)
			{
				return ERROR; /* Si no puede es porque tiene la mochila llena y por tanto no se coge el objeto y se devuelve error */
			}
			else
			{ /* En caso contrario se quita del espacio y se coge */
				space_del_object(playerSpace, object_get_id(game_get_object_at_name(game, object_name)));
				player_set_object(game->player, object_get_id(game_get_object_at_name(game, object_name)));
			}

			return OK;
		}
	}
	return ERROR;
}

STATUS game_callback_drop(Game *game)
{
	Id *idsObject = player_get_objects(game->player);
	int i, player_num_objects = player_get_num_of_objects(game->player);
	Space *playerSpace = game_get_space(game, game_get_player_location(game));
	char object_name[MAX_LEN];

	scanf("%s", object_name);
	object_name[strlen(object_name) + 1] = '\0';

	if (!idsObject)
	{
		return ERROR;
	}

	if (playerSpace == NULL)
	{
		return ERROR;
	}

	if (player_get_num_of_objects(game->player) == 0)
	{
		return ERROR;
	}

	else
	{
		for (i = 0; i < player_num_objects; i++)
		{
			if (strcmp(object_name, object_get_name(game_get_object_at_id(game, idsObject[i]))) == 0) /* Comprobamos si el nombre de los objetos que tiene el jugador en su mochila, coincide con el introducido para soltar */
			{

				space_set_object(playerSpace, object_get_id(game_get_object_at_id(game, idsObject[i])));
				player_del_object(game->player, idsObject[i]);

				/* Vemos si alguno de los objetos que porta el jugador depende del objeto soltado, y en ese caso lo soltamos también */
				for (int j = 0; j < MAX_OBJECTS; j++)
				{
					if (object_get_dependecy(game->object[j]) == object_get_id(game_get_object_at_name(game, object_name)) && player_has_object(game->player, object_get_id(game->object[j])))
					{
						space_set_object(playerSpace, object_get_id(game->object[j]));
						player_del_object(game->player, object_get_id(game->object[j]));
					}
				}

				game_manage_final(game); /* Comprobamos si dejo los ultimos objetos en la casilla final */
				return OK;
			}
		}
	}
	return ERROR;
}

STATUS game_callback_roll(Game *game)
{
	die_roll(game->die);
	return OK;
}

STATUS game_callback_move(Game *game)
{
	int i = 0;
	char movimiento[MAX_LEN];
	Id spaceId = NO_ID;
	Id currentId = NO_ID;
	spaceId = game_get_player_location(game);

	if (NO_ID == spaceId)
	{
		return ERROR;
	}

	scanf("%s", movimiento);
	movimiento[strlen(movimiento) + 1] = '\0';
	game_set_last_move(game, movimiento);

	for (i = 0; i < MAX_SPACES && game->spaces[i] != NULL; i++)
	{
		currentId = space_get_id(game->spaces[i]);
		if (currentId == spaceId)
		{
			if (strcmp(movimiento, "north") == 0 || strcmp(movimiento, "n") == 0)
			{
				currentId = link_get_pair_id(game_get_link_at_id(game, space_get_north(game->spaces[i])), spaceId);
				if (currentId != NO_ID && link_get_status(game_get_link_at_id(game, space_get_north(game->spaces[i]))) == ABIERTO)
				{
					game_set_player_location(game, currentId);
					game_manage_final(game); /* Gestionamos si el movimiento produce victoria/derrota */
					return OK;
				}
			}
			else if (strcmp(movimiento, "south") == 0 || strcmp(movimiento, "s") == 0)
			{
				currentId = link_get_second_id(game_get_link_at_id(game, space_get_south(game->spaces[i])));
				if (currentId != NO_ID && link_get_status(game_get_link_at_id(game, space_get_south(game->spaces[i]))) == ABIERTO)
				{
					game_set_player_location(game, currentId);
					game_manage_final(game); /* Gestionamos si el movimiento produce victoria/derrota */
					return OK;
				}
			}
			else if (strcmp(movimiento, "west") == 0 || strcmp(movimiento, "w") == 0)
			{
				currentId = link_get_pair_id(game_get_link_at_id(game, space_get_west(game->spaces[i])), spaceId);
				if (currentId != NO_ID && link_get_status(game_get_link_at_id(game, space_get_west(game->spaces[i]))) == ABIERTO)
				{
					game_set_player_location(game, currentId);
					game_manage_final(game); /* Gestionamos si el movimiento produce victoria/derrota */
					return OK;
				}
			}
			else if (strcmp(movimiento, "east") == 0 || strcmp(movimiento, "e") == 0)
			{
				currentId = link_get_pair_id(game_get_link_at_id(game, space_get_east(game->spaces[i])), spaceId);
				if (currentId != NO_ID && link_get_status(game_get_link_at_id(game, space_get_east(game->spaces[i]))) == ABIERTO)
				{
					game_set_player_location(game, currentId);
					game_manage_final(game); /* Gestionamos si el movimiento produce victoria/derrota */
					return OK;
				}
			}
			else if (strcmp(movimiento, "up") == 0 || strcmp(movimiento, "u") == 0)
			{
				currentId = link_get_pair_id(game_get_link_at_id(game, space_get_up(game->spaces[i])), spaceId);
				if (currentId != NO_ID && link_get_status(game_get_link_at_id(game, space_get_up(game->spaces[i]))) == ABIERTO)
				{
					game_set_player_location(game, currentId);
					game_manage_final(game); /* Gestionamos si el movimiento produce victoria/derrota */
					return OK;
				}
			}
			else if (strcmp(movimiento, "down") == 0 || strcmp(movimiento, "d") == 0)
			{
				currentId = link_get_pair_id(game_get_link_at_id(game, space_get_down(game->spaces[i])), spaceId);
				if (currentId != NO_ID && link_get_status(game_get_link_at_id(game, space_get_down(game->spaces[i]))) == ABIERTO)
				{
					game_set_player_location(game, currentId);
					game_manage_final(game); /* Gestionamos si el movimiento produce victoria/derrota */
					return OK;
				}
			}
		}
	}
	return ERROR;
}

STATUS game_callback_inspect(Game *game)
{
	Id currentLocation = NO_ID;
	char instruction[WORD_SIZE] = "";
	char strAux[WORD_SIZE + 1] = "";
	int i;

	if (!game)
	{
		return ERROR;
	}

	scanf("%s", instruction);
	currentLocation = game_get_player_location(game);
	if (space_get_iluminado(game_get_space(game, currentLocation)))
	{
		/* !< En caso de indicarse s o space significa que va a ser inspeccionado el space donde está situado el jugador */
		if (strcmp(instruction, "s") == 0 || strcmp(instruction, "space") == 0)
		{

			sprintf(strAux, "Space description-");
			strcat(strAux, space_get_longDescription(game_get_space(game, currentLocation)));

			if (space_get_iluminado(game_get_space(game, currentLocation)))
			{
				strcat(strAux, " (Is iluminated)");
			}
			else
			{
				strcat(strAux, " (Is not iluminated)");
			}

			strcpy(game->lastDescription, strAux);
			return OK;

			return ERROR;
		}
		else
		{ /* !< En caso contrtario significa que va a ser inspeccionado el objeto indicado por el nombre introducido */
			for (i = 0; i < MAX_OBJECTS; i++)
			{ /* !< Si está en el espacio o en el inventario */
				if (space_have_id(game_get_space(game, currentLocation), object_get_id(game->object[i])) || player_has_object(game->player, object_get_id(game->object[i])))
				{
					if (strcmp(object_get_name(game->object[i]), instruction) == 0)
					{
						sprintf(strAux, "%s-", instruction);
						strcat(strAux, object_get_description(game->object[i]));

						if (object_is_turnedon(game->object[i]))
						{
							strcat(strAux, ". Is turned On.");
						}
						else
						{
							strcat(strAux, ". Is not turned On.");
						}

						strcpy(game->lastDescription, strAux);
						return OK;
					}
				}
			}
		}
	}
	return ERROR;
}

STATUS game_callback_turnon(Game *game)
{

	char object_name[MAX_LEN];

	scanf("%s", object_name);
	object_name[strlen(object_name) + 1] = '\0';

	/* Si el jugador no tiene el objeto no lo podrá encender */
	if (!player_has_object(game->player, object_get_id(game_get_object_at_name(game, object_name))))
	{
		return ERROR;
	}

	/* Si el objeto no se puede iluminar se sale del callback */
	if (!object_get_iluminate(game_get_object_at_name(game, object_name)))
	{
		return ERROR;
	}

	/* Comprobar que el objeto no este encendido previamente y por tanto no podrá ser encendido de nuevo */
	if (object_is_turnedon(game_get_object_at_name(game, object_name)))
	{
		return ERROR;
	}

	/* Si no ocurre ninguna de las posibles situaciones de error, el objeto será encendido */
	object_set_turnedon(game_get_object_at_name(game, object_name), TRUE);

	return OK;
}

STATUS game_callback_turnoff(Game *game)
{

	char object_name[MAX_LEN];

	scanf("%s", object_name);
	object_name[strlen(object_name) + 1] = '\0';

	/* Si el jugador no tiene el objeto no lo podrá apagar */
	if (!player_has_object(game->player, object_get_id(game_get_object_at_name(game, object_name))))
	{
		return ERROR;
	}

	/* Si el objeto no se puede iluminar se sale del callback */
	if (!object_get_iluminate(game_get_object_at_name(game, object_name)))
	{
		return ERROR;
	}

	/* Comprobar que el objeto no este apagado previamente y por tanto no podrá ser apagado de nuevo */
	if (!object_is_turnedon(game_get_object_at_name(game, object_name)))
	{
		return ERROR;
	}

	/* Si no ocurre ninguna de las posibles situaciones de error, el objeto será apagado */
	object_set_turnedon(game_get_object_at_name(game, object_name), FALSE);

	return OK;
}

STATUS game_callback_open(Game *game)
{

	char object_name[MAX_LEN], link_name[MAX_LEN];
	Object *object = NULL;
	Link *link = NULL;

	scanf("%s with %s", link_name, object_name);
	object_name[strlen(object_name) + 1] = '\0';
	link_name[strlen(link_name) + 1] = '\0';

	if (!game)
	{
		return ERROR;
	}

	object = game_get_object_at_name(game, object_name);
	if (object == NULL)
	{
		return ERROR;
	}

	if (!player_has_object(game->player, object_get_id(object)))
	{
		return ERROR;
	}

	link = game_get_link_at_name(game, link_name);
	if (!link || link_get_status(link) == 0)
	{ /* Que el link ya estuviese abierto */
		return ERROR;
	}

	if (!object_open_link(object, link_get_id(link)))
	{ /* Si el link indicado no sea posible abrirlo con el objecto indicado */
		return ERROR;
	}

	return link_set_status(link, 0);
}

STATUS game_callback_save(Game *game)
{
	char file_name[MAX_LEN];
	scanf("%s", file_name);
	strcat(file_name, ".dat");

	if (game_management_save(game, file_name) == OK)
	{
		return OK;
	}
	else
	{
		return ERROR;
	}
}

STATUS game_callback_load(Game *game)
{
	char file_name[MAX_LEN];
	scanf("%s", file_name);
	strcat(file_name, ".dat");

	if (game_management_load(game, file_name) == OK)
	{
		return OK;
	}
	else
	{
		return ERROR;
	}
}
