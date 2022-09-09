#ifndef sockets_h
#define sockets_h

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

/********
* FUNCIÓN: int socket_create(int domain, int type, int protocol)
* ARGS_IN: char *request - petición de entrada (termina en \r\n)
* DESCRIPCIÓN: Procesa la petición de entrada, y blah, blah...
* ARGS_OUT: char * - devuelve un puntero señalado el verbo de la petición recibida
********/
int socket_create(int domain, int type, int protocol);
int socket_bind(int sockfd, int domain, int port, int addr);
int socket_listen(int sockfd, int backlog);
int socket_accept(int sockfd);
int socket_connect(int sockfd, int domain, int port, int addr);
int socket_read(int sockfd, void *buffer, int buffer_len);
int socket_write(int sockfd, void *buffer, int buffer_len);
void socket_close(int sockfd);

#endif