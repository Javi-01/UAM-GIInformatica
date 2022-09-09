/**
 * @brief Módulo que implementa las funciones relacionadas
 *        con la gestión de los sockets.
 *
 *
 * @file sockets.c
 * @author Pareja 1 del Grupo 2312 de Redes II
 * @version 1.0
 * @date 11-02-2022
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <syslog.h>

#include "sockets.h"

/**
 * FUNCIÓN: int socket_create(int domain, int type, int protocol)
 * ARGS_IN: int domain - el dominio del servidor
 *          int type - el tipo del servidor
 *          int protocol - el protocolo de comunicacion (TCP)
 * DESCRIPCIÓN: Crea un socket a partir de un cierto
 *              dominio, tipo y protocolo
 * ARGS_OUT: int sockfd - El descriptor de fichero que apunta al socket
 */
int socket_create(int domain, int type, int protocol)
{
    /*Se instancia el socket*/
    int sockfd = socket(domain, type, protocol);
    if (sockfd == -1)
    {
        syslog(LOG_ERR, "Error al crear el socket");
        return -1;
    }

    return sockfd;
}

/**
 * FUNCIÓN: int socket_bind(int sockfd, int domain, int port, int addr)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 *          int domain - el dominio del servidor
 *          int port - el puerto a hacer bind del socket
 *          int addr - la direccion del servidor
 * DESCRIPCIÓN: Registrar un socket a un determinado puerto y dirección
 * ARGS_OUT: 0 todo correcto, -1 en caso de error
 */
int socket_bind(int sockfd, int domain, int port, int addr)
{
    /*Se inicializa la estructura con la direccion del servidor*/
    struct sockaddr_in sock_addr;
    size_t ret;
    sock_addr.sin_family = domain;
    sock_addr.sin_port = htons(port);
    sock_addr.sin_addr.s_addr = htonl(addr);

    /*Se asigna esa direccion al socket del servidor*/

    if ((ret = bind(sockfd, (struct sockaddr *)&sock_addr, sizeof(sock_addr))) < 0)
    {
        syslog(LOG_ERR, "Error al asociar el puerto (bind) al socket");
        return -1;
    }

    return 0;
}

/**
 * FUNCIÓN: int socket_listen(int sockfd, int backlog)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 *          int backlog - tamaño de la cola de clientes
 * DESCRIPCIÓN: Crea una cola de clientes con capacidad maxima
 * ARGS_OUT: 0 todo correcto, -1 en caso de error
 */
int socket_listen(int sockfd, int backlog)
{
    /*Se asigna la cola de clientes al socket del servidor*/
    if (listen(sockfd, backlog) == -1)
    {
        syslog(LOG_ERR, "Error al intentar escuchar clientes");
        return -1;
    }

    return 0;
}

/**
 * FUNCIÓN: int socket_accept(int sockfd)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 * DESCRIPCIÓN: Acepta la conexion de un cliente para empezar la comunicacion
 * ARGS_OUT: conn_sockfd - el descriptor que apunta al nuevo socket que
 *              atenderá a esa conexión
 */
int socket_accept(int sockfd)
{
    int conn_sockfd;
    struct sockaddr_in sock_addr;

    unsigned int addr_length = sizeof(sock_addr);

    /*Se guarda en los parametros por argumentos la informacion del cliente, y nos
    devuelve el descriptor del nuevo socket en caso de conexion exitosa*/
    conn_sockfd = accept(sockfd, (struct sockaddr *)&sock_addr, &addr_length);
    if (conn_sockfd == -1)
    {
        syslog(LOG_ERR, "Error al aceptar la solicitud del cliente");
        return -1;
    }

    return conn_sockfd;
}

/**
 * FUNCIÓN: int socket_connect(int sockfd, int domain, int port, int addr)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
*           int domain - el dominio del servidor
 *          int port - el puerto del servidor
 *          int addr - la direccion del servidor
 * DESCRIPCIÓN: Envia una solicitud de conexion al servidor
 * ARGS_OUT: 0 todo correcto, -1 en caso de error
 */
int socket_connect(int sockfd, int domain, int port, int addr)
{

    /*Se inicializa la estructura con la direccion del servidor*/
    struct sockaddr_in sock_addr;
    sock_addr.sin_family = domain;
    sock_addr.sin_port = htons(port);
    sock_addr.sin_addr.s_addr = htonl(addr);

    /*Se guarda en los parametros por argumentos la informacion del servidor y
    nos conecta al mismo si todo ha salido correctamente*/
    if ((connect(sockfd, (struct sockaddr *)&sock_addr, sizeof(sock_addr))) == -1)
    {
        syslog(LOG_ERR, "Error al conectarse al servidor");
        return -1;
    }

    return 0;
}

/**
 * FUNCIÓN: int socket_read(int sockfd, void *buffer, int buffer_len)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 *          void *buffer - el buffer con el mensaje a leer
 *          int buffer_len - numero de bytes a leer
 * DESCRIPCIÓN: Lee el buffer de una conexion mediante sockets
 * ARGS_OUT: numero de bytes leidos o -1 en caso de error
 */
int socket_read(int sockfd, void *buffer, int buffer_len)
{
    int buff_bytes = -1;
    if ((buff_bytes = read(sockfd, buffer, buffer_len)) < 0)
    {
        syslog(LOG_ERR, "Error al leer el buffer");
        return -1;
    }
    return buff_bytes;
}

/**
 * FUNCIÓN: int socket_write(int sockfd, void *buffer, int buffer_len)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 *          void *buffer - el buffer con el mensaje a escribir
 *          int buffer_len - numero de bytes a escribir
 * DESCRIPCIÓN: Escribe por un buffer a la conexion mediante sockets
 * ARGS_OUT: numero de bytes escritos o -1 en caso de error
 */
int socket_write(int sockfd, void *buffer, int buffer_len)
{
    int buff_bytes = -1;
    if ((buff_bytes = write(sockfd, buffer, buffer_len)) < 0)
    {
        syslog(LOG_ERR, "Error al escribir en el buffer");
        return -1;
    }
    return buff_bytes;
}

/**
 * FUNCIÓN: void socket_close(int sockfd)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 * DESCRIPCIÓN: Cierra el descriptor de la conexion del socket
 * ARGS_OUT:
 */
void socket_close(int sockfd)
{
    close(sockfd);
}




