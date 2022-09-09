CC = gcc
CFLAGS = -g -Wall -pedantic

INCLUDE = ./includes
OBJ = ./obj
SRC = ./src
LIB = ./lib
SRCLIB = ./srclib

server: server.o sockets.o server_socket_conf.o picohttpparser.o process_http.o http_utils.o srclib.a
	$(CC) $(CFLAGS) ${OBJ}/server.o ${OBJ}/sockets.o ${OBJ}/picohttpparser.o ${OBJ}/server_socket_conf.o ${OBJ}/process_http.o ${OBJ}/http_utils.o ${LIB}/srclib.a -o $@  -lconfuse

server.o: ${SRC}/server.c
	$(CC) $(CFLAGS) -I${INCLUDE} -c ${SRC}/server.c -lpthread -o ${OBJ}/$@

picohttpparser.o: ${SRCLIB}/picohttpparser.c ${INCLUDE}/picohttpparser.h
	$(CC) $(CFLAGS) -I${INCLUDE} -c ${SRCLIB}/picohttpparser.c -o ${OBJ}/$@

sockets.o: ${SRCLIB}/sockets.c ${INCLUDE}/sockets.h
	$(CC) $(CFLAGS) -I${INCLUDE} -c ${SRCLIB}/sockets.c -o ${OBJ}/$@

process_http.o: ${SRC}/process_http.c ${INCLUDE}/process_http.h
	$(CC) $(CFLAGS) -I${INCLUDE} -c ${SRC}/process_http.c -o ${OBJ}/$@

http_utils.o: ${SRC}/http_utils.c ${INCLUDE}/http_utils.h
	$(CC) $(CFLAGS) -I${INCLUDE} -c ${SRC}/http_utils.c -o ${OBJ}/$@

server_socket_conf.o: ${SRC}/server_socket_conf.c ${INCLUDE}/server_socket_conf.h
	$(CC) $(CFLAGS) -I${INCLUDE} -c ${SRC}/server_socket_conf.c -o ${OBJ}/$@

srclib.a:
	ar -cvq ${LIB}/srclib.a ${OBJ}/sockets.o ${OBJ}/picohttpparser.o

valgrind:
	valgrind --leak-check=yes ./server

clean:
	rm -f ${OBJ}/*.o
	rm -f ${LIB}/*.a
	rm -f server