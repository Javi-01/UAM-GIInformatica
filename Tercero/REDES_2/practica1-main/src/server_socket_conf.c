/**
 * @brief Modulo para crear, registrar y poner a la espera un
 *        determinado socket
 *
 * @file server_socket_conf.c
 * @author Pareja 1 del Grupo 2312 de Redes II
 * @version 1.0
 * @date 12-02-2022
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

#include "sockets.h"
#include "server_socket_conf.h"

/**
 * FUNCIÓN: int server_socket_configuration(int domain, int type, int protocol, int port, int backlog)
 * ARGS_IN: int domain - el dominio del servidor
 *          int type - el tipo del servidor
 *          int protocol - el protocolo de comunicacion (TCP)
 *          int port - el puerto que utiliza el servidor
 *          int backlog - tamaño de la cola de clientes
 * DESCRIPCIÓN: Esta funcion se encarga de crear el socket, registrarlo al puerto del servidor,
 *              y establecerle la cola de clientes al servidor.
 *              La funcionalidad necesaria para poner el servidor en marcha
 * ARGS_OUT: int sockfd - El descriptor de fichero que apunta al socket
 */
int server_socket_configuration(int domain, int type, int protocol, int port, int backlog)
{
    /* Se crea el socket con la funcion creada en la libreria de sockets */
    int sockfd = socket_create(domain, type, protocol);
    if (sockfd == -1)
    {
        socket_close(sockfd);
        return -1;
    }

    /* Se hace bind del socket y se establece la addr que coja el sistema */
    if (socket_bind(sockfd, domain, port, INADDR_ANY) == -1)
    {
        socket_close(sockfd);
        return -1;
    }

    /* LLamamos a la funcion de escucha de clientes */
    if (socket_listen(sockfd, backlog) == -1)
    {
        socket_close(sockfd);
        return -1;
    }
    return sockfd;
}
