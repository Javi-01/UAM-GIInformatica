#ifndef http_utils_h
#define http_utils_h

#define DATE_SIZE 100


char *http_actual_date();
char *http_last_modified(char *file);
int http_content_length(char *file);
char *http_content_type(char *extension);

#endif