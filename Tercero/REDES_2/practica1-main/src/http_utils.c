/**
 * @brief Modulo con las utilidades necesarias de los campos headers
 *          de las respuestas http.
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
#include <time.h>
#include <sys/types.h>
#include <sys/stat.h>

#include "http_utils.h"

/**
 * FUNCIÓN: char *http_actual_date()
 * ARGS_IN:
 * DESCRIPCIÓN: Funcion que obtiene en el formato reconocido por HTTP la fecha
 *              actual del sistema
 * ARGS_OUT: char * - puntero a cadena con la fecha o NULL en caso de error
 */
char *http_actual_date()
{
    /* Se obtiene el tiempo actual */
    time_t actual_time = time(NULL);
    struct tm local_time = *localtime(&actual_time);

    char *formatted_date = (char *)malloc(DATE_SIZE * sizeof(char));
    if (!formatted_date)
    {
        return NULL;
    }

    /* Se formatea la fecha */
    strftime(formatted_date, DATE_SIZE, "%a, %d %b %Y %H:%M:%S %Z", &local_time);

    return formatted_date;
}

/**
 * FUNCIÓN: char *http_last_modified(char *file)
 * ARGS_IN: char *file - cadena con la ruta del fichero
 * DESCRIPCIÓN: Funcion que obtiene en el formato reconocido por HTTP la fecha
 *              de la ultima modificacion del fichero
 * ARGS_OUT: char * - puntero a cadena con la fecha o NULL en caso de error
 */
char *http_last_modified(char *file)
{
    char *modified_date = (char *)malloc(DATE_SIZE * sizeof(char));
    if (!modified_date)
    {
        return NULL;
    }

    struct stat st;
    if (!stat(file, &st))
    {
        /* Se formatea la fecha */
        strftime(modified_date, DATE_SIZE, "%a, %d %b %Y %H:%M:%S %Z", localtime(&(st.st_mtime)));
        return modified_date;
    }

    return NULL;
}

/**
 * FUNCIÓN: int http_content_length(char *file)
 * ARGS_IN: char *file - cadena con la ruta del fichero
 * DESCRIPCIÓN: Funcion que obtiene el tamaño en bytes del contenido del fichero
 * ARGS_OUT: int - tamaño en bytes del fichero o -1 en caso de error
 */
int http_content_length(char *file)
{
    struct stat st;
    if (!stat(file, &st))
    {
        return st.st_size;
    }
    return -1;
}

/**
 * FUNCIÓN: char *http_content_type(char *extension)
 * ARGS_IN: char *extension - extension del fichero
 * DESCRIPCIÓN: Funcion que dependiendo de la extension del fichero, devuelve
 *              el campo content-type de la respuesta HTTP.
 * ARGS_OUT: char * - cadena con el campo content-type o NULL en caso de error
 */
char *http_content_type(char *extension)
{
    if (!extension)
    {
        return NULL;
    }
    if (strcmp(extension, ".txt") == 0)
    {
        return "text/plain";
    }
    else if (strcmp(extension, ".html") == 0 || strcmp(extension, ".htm") == 0 || strcmp(extension, ".py") == 0 || strcmp(extension, ".php") == 0)
    {
        return "text/html";
    }
    else if (strcmp(extension, ".gif") == 0)
    {
        return "image/gif";
    }
    else if (strcmp(extension, ".jpeg") == 0 || strcmp(extension, ".jpg") == 0)
    {
        return "image/jpeg";
    }
    else if (strcmp(extension, ".mpeg") == 0 || strcmp(extension, ".mpg") == 0)
    {
        return "video/mpeg";
    }
    else if (strcmp(extension, ".doc") == 0 || strcmp(extension, ".docx") == 0)
    {
        return "application/msword";
    }
    else if (strcmp(extension, ".pdf") == 0)
    {
        return "application/pdf";
    }
    else
    {
        return NULL;
    }
}
