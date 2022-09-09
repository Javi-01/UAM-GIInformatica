/**
 * @brief Modulo que se encarga de procesar peticiones
 *        para generar respuestas
 *
 * @file process_http.c
 * @author Pareja 1 del Grupo 2312 de Redes II
 * @version 1.0
 * @date 18-03-2022
 * @copyright GNU Public License
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <syslog.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>

#include "process_http.h"
#include "picohttpparser.h"
#include "sockets.h"

/* Funciones privadas (comentadas donde estan implementadas) */
int search_request_args(char *parse_path, int be_args, Request_args *args, char *post_args);
void free_struct(Request_args *args);

/**
 * FUNCIÓN: void process_http_request(char *buffer, int sockfd, char *server_signature, char *server_root)
 * ARGS_IN: void *buffer - el buffer con el mensaje a leer
 *          int sockfd - descriptor de fichero de socket
 *          char *server_signature - información acerca del software usado por el servidor original encargado de la solicitud
 *          char *server_root - URL del servidor (en nuestro caso es ./www)
 * DESCRIPCIÓN: Recibe una peticiones y se encarga de parsear la root,
 *              para obtener el método y comprobar si es válido, la extensión
 *              del fichero y si tienes argumentos en el cuerpo
 * ARGS_OUT:
 */
void process_http_request(char *buffer, int sockfd, char *server_signature, char *server_root)
{
    /* Variables de la peticion HTTPparser */
    char *parse_method, *method, *parse_path;
    int request_len, http_version;
    struct phr_header parse_headers[PARSE_HEADER];
    size_t method_len, path_len, num_headers;

    if ((sockfd == -1) | !server_signature | !server_root)
    {
        syslog(LOG_ERR, "Fallo en la comprobacion de argumentos a la hora de procesar una respuesta POST\n");
        return;
    }

    /* Variables necesarias para crear respuesta */
    int be_args = 0;
    char body_parse[MAX_CAD_LEN], path_aux[MAX_CAD_LEN], path[MAX_CAD_LEN], body[MAX_CAD_LEN], uri[MAX_CAD_LEN], uri_aux[MAX_CAD_LEN], extension[MAX_CAD_LEN];

    memset(extension, 0, sizeof(extension));

    /* Parseamos la peticion HTTP */
    num_headers = sizeof(parse_headers) / sizeof(parse_headers[0]);
    request_len = phr_parse_request(buffer, (size_t)buffer, (const char **)&parse_method, &method_len, (const char **)&parse_path, &path_len,
                                    &http_version, parse_headers, &num_headers, (size_t)0);

    if (request_len == -1)
    {
        syslog(LOG_ALERT, "Request_length 0");
    }

    if (!(method = (char *)malloc((method_len + 1) * sizeof(char))))
    {
        free(method);
        return;
    }

    /* Se crea la uri a partir del path establecido en el config y el path de la url */
    sprintf(method, "%.*s", (int)method_len, parse_method);
    sprintf(path, "%.*s", (int)path_len, parse_path);

    if (strcmp(method, POST) == 0)
    {
        sprintf(body_parse, "%s", buffer);

        /* Obtenemos el body de la peticion POST */
        char *post_body = strstr(body_parse, "\r\n\r\n");
        if (post_body != NULL)
        {
            char *b = strtok(post_body, "\r\n\r\n");
            if (b != NULL)
            {
                strcpy(body, b);
                be_args = 1;
                b = strtok(NULL, "\r\n\r\n");
            }
            free(b);
        }
    }

    /* Parseamos la ruta y obtenemos de ella su extensión,
    siendo independiente que la ruta tenga o no args */
    sprintf(path_aux, "%.*s", (int)path_len, parse_path);

    char *parse = strtok(path_aux, ".?");
    if (parse != NULL)
    {
        int i = 0;
        while (parse != NULL)
        {
            i += 1;
            if (i == 2)
            {
                sprintf(extension, ".%s", parse);
            }
            parse = strtok(NULL, ".?");
        }
    }
    free(parse);

    /* Crear ruta por defecto */
    if (strcmp(path, "/") == 0)
    {
        sprintf(path, "/index.html");
        sprintf(extension, ".html");
    }

    /* Obtenemos la path del archivo sin posibles args en la url
    y la path con args para usarse despues */
    strcpy(uri, server_root);
    strcat(uri, path);
    strcpy(uri_aux, uri);

    parse = strtok(uri_aux, "?");
    if (parse != NULL)
    {
        int i = 0;
        while (parse != NULL)
        {
            i += 1;
            if (i == 1)
            {
                sprintf(uri, "%s", parse);
            }
            parse = strtok(NULL, "?");
        }
    }

    /* Se procesa el tipo de peticion que es y si es un método válido */
    if (strcmp(method, GET) == 0)
    {
        process_http_GET_response(sockfd, uri, path, extension, buffer, server_signature, http_version);
    }
    else if (strcmp(method, POST) == 0)
    {
        process_http_POST_response(sockfd, uri, path, body, be_args, extension, buffer, server_signature, http_version);
    }
    else if (strcmp(method, OPTIONS) == 0)
    {
        process_http_OPTIONS_response(sockfd, server_signature, http_version);
    }
    else
    {
        process_400_error_response(sockfd, server_signature, http_version);
    }

    free(method);
    free(parse);

    return;
}

/**
 * FUNCIÓN: process_http_GET_response(int sockfd, char *path, char *parse_path, char *extension, char *buffer, char *server_signature, int http_version)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 *          char *path - URL del recurso solicitado
 *          char *parse_path - URL junto con posibles argumentos en ella
 *          char *extension - extensión del script a ejecutar
 *          void *buffer - el buffer con el mensaje a leer
 *          char *server_signature - información acerca del software usado por el servidor original encargado de la solicitud
 *          int http_version - indica la versión de HTTP (versión 0 o 1 para HTTP 1.X)
 * DESCRIPCIÓN: Procesar una petición GET y generar una respuesta GET
 * ARGS_OUT:
 */
void process_http_GET_response(int sockfd, char *path, char *parse_path, char *extension, char *buffer, char *server_signature, int http_version)
{
    char buffer_out[BUFFER_LEN], buffer_in[BUFFER_LEN];
    char *actual_date, *modified_date, content_type[MAX_CAD_LEN];
    int size_file = -1, read_len;
    FILE *filefd = NULL;

    if ((sockfd == -1) | !server_signature)
    {
        syslog(LOG_ERR, "Fallo en la comprobacion de argumentos a la hora de procesar una respuesta GET\n");
        return;
    }

    memset(buffer_out, 0, sizeof(buffer_out));

    if (!(filefd = fopen(path, "rb")))
    {
        process_404_error_response(sockfd, server_signature, http_version);
        return;
    }

    if (!(actual_date = http_actual_date()))
    {
        fclose(filefd);
        return;
    }

    if (!(modified_date = http_last_modified(path)))
    {
        fclose(filefd);
        free(actual_date);
        return;
    }
    if ((size_file = http_content_length(path)) == -1)
    {
        fclose(filefd);
        free(actual_date);
        free(modified_date);
        return;
    }

    if (!(strcpy(content_type, http_content_type(extension))))
    {
        fclose(filefd);
        free(actual_date);
        free(modified_date);
        return;
    }

    if ((strcmp(extension, ".py")) == 0 || (strcmp(extension, ".php")) == 0)
    {
        char script_content[MAX_CAD_LEN];
        Request_args *args; // struc para los args

        args = (Request_args *)malloc(sizeof(Request_args));

        // Buscamos si hay args en la URL y en el body
        if (search_request_args(parse_path, 0, args, " ") == -1)
        {
            syslog(LOG_ERR, "Fallo al buscar argumentos en la peticion");
            free(actual_date);
            free(modified_date);
            free_struct(args);
            fclose(filefd);
            return;
        }

        // Procesamos el script
        if (proccess_script(sockfd, server_signature, args, http_version, path, extension, script_content) == -1)
        {
            syslog(LOG_ERR, "Fallo tras querer ejecutar un script con extension %s", extension);
            free(actual_date);
            free(modified_date);
            fclose(filefd);
            free_struct(args);

            return;
        }

        /* Se contruye la respuesta HTTP */
        sprintf(buffer_out, "HTTP/1.%d 200 OK\r\nDate: %s\r\nServer: %s\r\nLast-Modified: %s\r\nContent-Length: %d\r\nConnection: close\r\nContent-Type: %s\r\n\r\n <!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head><body><pre>%s</pre></body></html>", http_version, actual_date, server_signature, modified_date, size_file, content_type, script_content);

        free_struct(args);

        /* Se envia al cliente via sockets */
        socket_write(sockfd, buffer_out, sizeof(buffer_out));
    }
    else
    {
        sprintf(buffer_out, "HTTP/1.%d 200 OK\r\nDate: %s\r\nServer: %s\r\nLast-Modified: %s\r\nContent-Length: %d\r\nConnection: close\r\nContent-Type: %s\r\n\r\n", http_version, actual_date, server_signature, modified_date, size_file, content_type);

        /* Se envia al cliente via sockets */
        socket_write(sockfd, buffer_out, strlen(buffer_out));
    }

    fseek(filefd, 0, SEEK_SET);

    /* Se envia el recurso que se ha solicitado */
    while (!(feof(filefd)))
    {
        read_len = fread(buffer_in, sizeof(char), BUFFER_LEN, filefd);
        socket_write(sockfd, buffer_in, read_len);
        bzero(buffer_in, sizeof(buffer_in));
    }

    free(actual_date);
    free(modified_date);
    fclose(filefd);

    return;
}

/**
 * FUNCIÓN: void process_http_POST_response(int sockfd, char *path, char *parse_path, char *body_args, int be_args, char *extension, char *buffer, char *server_signature, int http_version)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 *          char *path - URL del recurso solicitado
 *          char *parse_path - URL junto con posibles argumentos en ella
 *          char *body_args - cadena con los argumentos que pudiera haber en el body de la petición
 *          int be_args - indica si hay argumentos en el body, 0 no hay y 1 si hay
 *          char *extension - extensión del script a ejecutar
 *          void *buffer - el buffer con el mensaje a leer
 *          char *server_signature - información acerca del software usado por el servidor original encargado de la solicitud
 *          int http_version - indica la versión de HTTP (versión 0 o 1 para HTTP 1.X)
 * DESCRIPCIÓN: Procesar una petición POST y generar una respuesta POST
 * ARGS_OUT:
 */
void process_http_POST_response(int sockfd, char *path, char *parse_path, char *body_args, int be_args, char *extension, char *buffer, char *server_signature, int http_version)
{
    char buffer_out[BUFFER_LEN], buffer_in[BUFFER_LEN];
    char *actual_date, *modified_date, content_type[MAX_CAD_LEN];
    int size_file = -1, read_len;
    FILE *filefd = NULL;

    if ((sockfd == -1) | !server_signature)
    {
        syslog(LOG_ERR, "Fallo en la comprobacion de argumentos a la hora de procesar una respuesta POST\n");
        return;
    }

    memset(buffer_out, 0, sizeof(buffer_out));

    if (!(filefd = fopen(path, "rb")))
    {
        process_404_error_response(sockfd, server_signature, http_version);
        return;
    }

    if (!(actual_date = http_actual_date()))
    {
        fclose(filefd);
        return;
    }

    if (!(modified_date = http_last_modified(path)))
    {
        fclose(filefd);
        free(actual_date);
        return;
    }

    if ((size_file = http_content_length(path)) == -1)
    {
        fclose(filefd);
        free(actual_date);
        free(modified_date);
        return;
    }

    if (!(strcpy(content_type, http_content_type(extension))))
    {
        fclose(filefd);
        free(actual_date);
        free(modified_date);
        return;
    }

    if ((strcmp(extension, ".py")) == 0 || (strcmp(extension, ".php")) == 0)
    {
        char script_content[MAX_CAD_LEN];
        Request_args *args; // struc para los args

        args = (Request_args *)malloc(sizeof(Request_args));

        // Buscamos si hay args en la URL y en el body
        if (search_request_args(parse_path, be_args, args, body_args) == -1)
        {
            syslog(LOG_ERR, "Fallo al buscar argumentos en la peticion");
            free(actual_date);
            free(modified_date);
            free_struct(args);
            fclose(filefd);
            return;
        }

        // Procesamos el script
        if (proccess_script(sockfd, server_signature, args, http_version, path, extension, script_content) == -1)
        {
            syslog(LOG_ERR, "Fallo tras querer ejecutar un script con extension %s", extension);
            free(actual_date);
            free(modified_date);
            free_struct(args);
            fclose(filefd);
            return;
        }

        /* Se contruye la respuesta HTTP */
        sprintf(buffer_out, "HTTP/1.%d 200 OK\r\nDate: %s\r\nServer: %s\r\nLast-Modified: %s\r\nContent-Length: %d\r\nConnection: close\r\nContent-Type: %s\r\n\r\n <!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head><body><pre>%s</pre></body></html>", http_version, actual_date, server_signature, modified_date, size_file, content_type, script_content);

        free_struct(args);

        /* Se envia al cliente via sockets */
        socket_write(sockfd, buffer_out, sizeof(buffer_out));
    }
    else
    {
        sprintf(buffer_out, "HTTP/1.%d 200 OK\r\nDate: %s\r\nServer: %s\r\nLast-Modified: %s\r\nContent-Length: %d\r\nConnection: close\r\nContent-Type: %s\r\n\r\n", http_version, actual_date, server_signature, modified_date, size_file, content_type);

        /* Se envia al cliente via sockets */
        socket_write(sockfd, buffer_out, strlen(buffer_out));
    }

    fseek(filefd, 0, SEEK_SET);

    /* Se envia el recurso que se ha solicitado */
    while (!(feof(filefd)))
    {
        read_len = fread(buffer_in, sizeof(char), BUFFER_LEN, filefd);
        socket_write(sockfd, buffer_in, read_len);
        bzero(buffer_in, sizeof(buffer_in));
    }

    free(actual_date);
    free(modified_date);
    fclose(filefd);

    return;
}

/**
 * FUNCIÓN: void process_http_OPTIONS_response(int sockfd, char *server_signature, int http_version)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 *          char *server_signature - información acerca del software usado por el servidor original encargado de la solicitud
 *          int http_version - indica la versión de HTTP (versión 0 o 1 para HTTP 1.X)
 * DESCRIPCIÓN: Procesar una petición OPTIONS y generar una respuesta OPTIONS
 * ARGS_OUT:
 */
void process_http_OPTIONS_response(int sockfd, char *server_signature, int http_version)
{
    char buffer_out[BUFFER_LEN];
    char *actual_date;

    if ((sockfd == -1) | !server_signature)
    {
        syslog(LOG_ERR, "Fallo en la comprobacion de argumentos a la hora de procesar una respuesta OPTIONS\n");
        return;
    }

    if (!(actual_date = http_actual_date()))
    {
        free(actual_date);
        return;
    }

    /* Se contruye la respuesta HTTP */
    sprintf(buffer_out, "HTTP/1.%d 200 OK\r\nDate: %s\r\nServer: %s\r\nAllow: GET POST OPTIONS\r\nContent-Length: 0\r\nConnection: close\r\nContent-Type: text/html\r\n", http_version, actual_date, server_signature);

    /* Se envia al cliente via sockets */
    socket_write(sockfd, buffer_out, sizeof(buffer_out));
    free(actual_date);
    return;
}

/**
 * FUNCIÓN: int proccess_script(int sockfd, char *server_signature, Request_args *args, int http_version, char *path, char *extension, char *response)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 *          char *server_signature - información acerca del software usado por el servidor original encargado de la solicitud
 *          Request_args *args - struct que almacena una lista de argumentos que provienen de la petición y el número de ellos
 *          int http_version - indica la versión de HTTP (versión 0 o 1 para HTTP 1.X)
 *          char *path - ruta donde se encuentra el archivo
 *          char *extension - extensión del script a ejecutar
 *          char *response - cadena que contiene la salida del script ejecutado
 * DESCRIPCIÓN: Procesar un script con extensión .py o .php con o sin argumentos
 * ARGS_OUT: 0 todo correcto, -1 en caso de error
 */
int proccess_script(int sockfd, char *server_signature, Request_args *args, int http_version, char *path, char *extension, char *response)
{
    FILE *file;
    int filefd = 0;
    int response_len = 0;
    char command_to_execute[MAX_CAD_LEN], execute[MAX_CAD_LEN + MAX_CAD_LEN], buffer[BUFFER_LEN];

    if (!path | !extension | !response)
    {
        syslog(LOG_ERR, "Fallo en la comprobacion de argumentos a la hora de procesar un script\n");
        return -1;
    }

    /* Miramos la extension para saber que comando ejecutar */
    if (strcmp(extension, ".py") == 0)
    {
        sprintf(command_to_execute, "python3");
    }
    else
    {
        strcpy(command_to_execute, "php");
    }

    sprintf(execute, "%s %s", command_to_execute, path);
    if (args->num_args > 0)
    {
        for (int i = 0; i < args->num_args; i++)
        {
            strcat(execute, " ");
            strcat(execute, args->args[i]);
        }
    }

    /* Ejecutamos el script */
    if (!(file = popen(execute, "r")))
    {
        syslog(LOG_ERR, "Fallo a la hora de hacer un popen sobre el script a ejecutar\n");
        return -1;
    }

    /* Leemos el retorno de la ejecucion del script */
    while (!feof(file))
    {
        response_len = fread(buffer, sizeof(char), MAX_CAD_LEN, file);

        if (response_len < 0)
        {
            syslog(LOG_ERR, "Fallo a la hora de ejecutar el script\n");
            pclose(file);
            close(filefd);
            return -1;
        }
        else if (response_len > 0)
        {
            strcpy(response, "");
            strncat(response, buffer, response_len);
            strcat(response, "\n");
        }
    }

    pclose(file);
    return 0;
}

/**
 * FUNCIÓN: void process_400_error_response(int sockfd, char *server_signature, int http_version)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 *          char *server_signature - información acerca del software usado por el servidor original encargado de la solicitud
 *          int http_version - indica la versión de HTTP (versión 0 o 1 para HTTP 1.X)
 * DESCRIPCIÓN: Procesar una respuesta de error 404
 * ARGS_OUT:
 */
void process_404_error_response(int sockfd, char *server_signature, int http_version)
{
    char buffer_out[BUFFER_LEN];
    char *actual_date;
    int size_file = -1;
    char file_content[] = "<!DOCTYPE html><html><head><title>Document</title></head><body><h1>Error 404 : Not Found</h1></body></html>";

    if ((sockfd == -1) | !server_signature)
    {
        syslog(LOG_ERR, "Fallo en la comprobacion de argumentos a la hora de procesar el error 404\n");
        return;
    }

    if (!(actual_date = http_actual_date()))
    {
        free(actual_date);
        return;
    }

    size_file = strlen(file_content);
    /* Se contruye la respuesta HTTP */
    sprintf(buffer_out, "HTTP/1.%d 404 Not Found\r\nDate: %s\r\nServer: %s\r\nLast-Modified: %s\r\nContent-Length: %d\r\nConnection: close\r\nContent-Type: text/html\r\n\r\n%s", http_version, actual_date, server_signature, actual_date, size_file, file_content);

    socket_write(sockfd, buffer_out, strlen(buffer_out));
    free(actual_date);
    return;
}

/**
 * FUNCIÓN: void process_400_error_response(int sockfd, char *server_signature, int http_version)
 * ARGS_IN: int sockfd - descriptor de fichero de socket
 *          char *server_signature - información acerca del software usado por el servidor original encargado de la solicitud
 *          int http_version - indica la versión de HTTP (versión 0 o 1 para HTTP 1.X)
 * DESCRIPCIÓN: Procesar una respuesta de error 400
 * ARGS_OUT:
 */
void process_400_error_response(int sockfd, char *server_signature, int http_version)
{
    char buffer_out[BUFFER_LEN];
    char *actual_date;
    int size_file = -1;
    char file_content[] = "<!DOCTYPE html><html><head><title>Document</title></head><body><h1>Error 400 : Bad Request</h1></body></html>";

    if ((sockfd == -1) | !server_signature)
    {
        syslog(LOG_ERR, "Fallo en la comprobacion de argumentos a la hora de procesar el error 400\n");
        return;
    }

    if (!(actual_date = http_actual_date()))
    {
        free(actual_date);
        return;
    }

    size_file = strlen(file_content);
    /* Se contruye la respuesta HTTP */
    sprintf(buffer_out, "HTTP/1.%d 400 Bad Request\r\nDate: %s\r\nServer: %s\r\nLast-Modified: %s\r\nContent-Length: %d\r\nConnection: close\r\nContent-Type: text/html\r\n\r\n%s", http_version, actual_date, server_signature, actual_date, size_file, file_content);

    socket_write(sockfd, buffer_out, strlen(buffer_out));
    free(actual_date);
    return;
}

/**
 * FUNCIÓN: void search_request_args(char *parse_path, int be_args, Request_args *args, char *post_args)
 * ARGS_IN: char *parse_path- URL con posibles argumentos pasados por la misma a partir del ?
 *          int be_args - indica si hay argumentos en el body, 0 no hay y 1 si hay
 *          Request_args *args - struct que almacena una lista de argumentos que provienen de la petición y el número de ellos
 *          char *post_args - cadena con los argumentos que pudiera haber en el body de la petición
 * DESCRIPCIÓN: Buscar argumentos tanto en la URL como en el body de la petición
 * ARGS_OUT: 0 todo correcto, -1 en caso de error
 */
int search_request_args(char *parse_path, int be_args, Request_args *args, char *post_args)
{
    char cad[MAX_CAD_LEN], cad_aux[MAX_CAD_LEN], args_aux[MAX_CAD_LEN];
    int i;

    if (!parse_path | !args)
    {
        syslog(LOG_ERR, "Fallo en la comprobacion de argumentos a la hora de buscar argumentos en la peticion\n");
        return -1;
    }

    memset(cad, 0, sizeof(cad));
    memset(cad_aux, 0, sizeof(cad_aux));

    args->num_args = 0;

    /* Obtenemos el numero de argumentos y los propios argumentos de la peticion
    en el caso de que sea una petición GET/POST */
    char *parse = strtok(parse_path, "?");
    if (parse != NULL)
    {
        i = 0;
        while (parse != NULL)
        {
            i += 1;
            if (i == 2)
            {
                strcpy(cad, parse);
                strcpy(cad_aux, parse);
            }
            parse = strtok(NULL, "?");
        }
    }
    free(parse);

    parse = strtok(cad, "&");
    if (parse != NULL)
    {
        while (parse != NULL)
        {
            parse = strtok(NULL, "&");
            args->num_args += 1;
        }
    }
    free(parse);

    /* Si hay args en el body se comprueba el numero de ellos */
    if (be_args == 1)
    {
        strcpy(args_aux, post_args);
        parse = strtok(post_args, "&");
        if (parse != NULL)
        {
            while (parse != NULL)
            {
                parse = strtok(NULL, "&");
                args->num_args += 1;
            }
        }
    }
    free(parse);

    /* Reservamos memoria para la lista de posibles args */
    args->args = (char **)malloc(args->num_args * sizeof(char *));

    /* Reservamos una matriz para guardar en ella los argumentos */
    for (i = 0; i < args->num_args; i++)
        args->args[i] = (char *)malloc(MAX_CAD_LEN * sizeof(char));

    if (args->num_args > 0)
    {
        i = 0;
        parse = strtok(cad_aux, "=&");
        if (parse != NULL)
        {
            int j = 0;
            while (parse != NULL)
            {
                j += 1;
                if ((j % 2) == 0)
                {
                    sprintf(args->args[i], "%s", parse);
                    i += 1;
                }
                parse = strtok(NULL, "=&");
            }
        }

        /* Si hay args en el body los guardamos */
        if (be_args == 1)
        {
            free(parse);
            parse = strtok(args_aux, "=&");
            if (parse != NULL)
            {
                int j = 0;
                while (parse != NULL)
                {
                    j += 1;
                    if ((j % 2) == 0)
                    {
                        sprintf(args->args[i], "%s", parse);
                        i += 1;
                    }
                    parse = strtok(NULL, "=&");
                }
            }
        }
    }

    free(parse);

    return 0;
}

/**
 * FUNCIÓN: void free_struct(Request_args *args)
 * ARGS_IN: Request_args *args - struct que almacena una lista de argumentos que provienen de la petición y el número de ellos
 * DESCRIPCIÓN: Liberar la estrcutura Request_args
 * ARGS_OUT:
 */
void free_struct(Request_args *args)
{
    if(!args)
    {
        syslog(LOG_ERR, "Fallo al liberar memoria de la struct Request_args\n");
        return;
    }

    for (int i = 0; i < args->num_args; i++)
    {
        free(args->args[i]);
    }
    free(args->args);
    free(args);
}