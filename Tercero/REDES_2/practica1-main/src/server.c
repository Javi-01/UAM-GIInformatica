/**
 * @brief Modulo que implementa un servidor HTTP recursivo.
 *        Soporta peticiones GET, POST y OPTIONS.
 *
 * @file server.c
 * @author Pareja 1 del Grupo 2312 de Redes II
 * @version 1.0
 * @date 18-02-2022
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <syslog.h>
#include <pthread.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>

#include "sockets.h"
#include "confuse.h"
#include "server_socket_conf.h"
#include "process_http.h"

/* Inicializamos variables de configuracion del servidor */
static char *server_root = NULL;
static long int max_clients = 0;
static long int listen_port = 0;
static char *server_signature = NULL;

/** FUNCIONES PRIVADAS **/

/**
 * FUNCIÓN: manejador_SIGINT(int signal)
 * ARGS_IN: int signal - señal capturada
 * DESCRIPCIÓN: Manejador de la señal SIGINT
 * ARGS_OUT:
 */
void manejador_SIGINT(int signal);

/* Estructura del fileParser */
cfg_t *cfg;

int server_socket = -1, connection_socket = -1;

int main(void)
{

    /* Variables de la peticion HTTPparser */
    char buff[4096];
    struct sigaction act;
    pid_t pid;

    memset(buff, 0, sizeof(buff));

    /* Inicializamos el conjunto de señales al de la señal SIGINT */
    sigemptyset(&(act.sa_mask));
    act.sa_flags = SIGINT;

    /* Se arma la señal SIGINT */
    act.sa_handler = manejador_SIGINT;
    if ((sigaction(SIGINT, &act, NULL) < 0))
    {
        perror("sigaction SIGINT");
        exit(EXIT_FAILURE);
    }

    /* Parseo del fichero server.conf con la libreria confuse.h */
    cfg_opt_t opts[] = {
        CFG_SIMPLE_STR("server_root", &server_root),
        CFG_SIMPLE_INT("max_clients", &max_clients),
        CFG_SIMPLE_INT("listen_port", &listen_port),
        CFG_SIMPLE_STR("server_signature", &server_signature),
        CFG_END()};

    cfg = cfg_init(opts, 0);

    if (cfg_parse(cfg, "server.conf") == CFG_FILE_ERROR)
    {
        syslog(LOG_ERR, "Error al hacer cfg_parse()");
        cfg_free(cfg);
        return -1;
    }

    /* Se crea el socket del servidor, se
    registran direcciones y se establece la cola de clientes */
    server_socket = server_socket_configuration(AF_INET, SOCK_STREAM, 0, listen_port, max_clients);
    if (server_socket == -1)
    {
        printf("[SERVER-ERROR] => Error en la configuracion del server\n");
        return -1;
    }
    else
    {
        printf("[SERVER] => Servidor iniciado\n");
    }
    while (1)
    {
        if ((connection_socket = socket_accept(server_socket)) == -1)
        {
            printf("[SERVER-ERROR] => Conexion con cliente fallida\n");
        }else
        {
            /* Creamos el proceso hijo */
            pid = fork();
            if (pid == 0)
            {
                /* Se lee el buffer del socket*/
                if ((socket_read(connection_socket, buff, sizeof(buff))) < 0)
                {
                    socket_close(connection_socket);
                    break;
                }
                else
                {
                    /* Se manda a procesar la peticion HTTP y crear la respuesta */
                    process_http_request(buff, connection_socket, server_signature, server_root);
                    bzero(buff, sizeof(buff));
                }
            }
        }
    }

    free(server_root);
    free(server_signature);
    cfg_free(cfg);
    socket_close(connection_socket);

    return 0;
}

void manejador_SIGINT(int signal)
{
    socket_close(connection_socket);
    socket_close(server_socket);
    free(server_root);
    free(server_signature);
    cfg_free(cfg);
    exit(EXIT_SUCCESS);
}
