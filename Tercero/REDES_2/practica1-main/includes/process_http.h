#ifndef process_http_h
#define process_http_h

#include "http_utils.h"

#define GET "GET"
#define POST "POST"
#define OPTIONS "OPTIONS"
#define URI_SIZE 70
#define BUFFER_LEN 4096
#define MAX_CAD_LEN 1024
#define PARSE_HEADER 100

typedef struct
{
    char **args;
    int num_args;
} Request_args;

void process_http_request(char *buffer, int sockfd, char *server_signature, char *server_root);

void process_http_GET_response(int sockfd, char *path, char *parse_path, char * extension, char *buffer, char *server_signature, int http_version);
void process_http_POST_response(int sockfd, char *path, char *parse_path, char *body_args, int be_args, char *extension, char *buffer, char *server_signature, int http_version);
void process_http_OPTIONS_response(int sockfd, char *server_signature, int http_version);

void process_404_error_response(int sockfd, char *server_signature, int http_version);
void process_400_error_response(int sockfd, char *server_signature, int http_version);

int proccess_script(int sockfd, char *server_signature, Request_args *args, int http_version, char *path, char * extension, char *response);

#endif
