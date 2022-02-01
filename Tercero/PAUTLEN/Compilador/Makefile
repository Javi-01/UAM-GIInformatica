CC = gcc
BISON = bison
FLEX = flex
ALFA=./alfa

BIN = alfa
CFLAGS = -Wall
CYYFLAGS =

FLEXFLAGS =

BISONFLAGS = -dyv

OBJ = tablaSimbolos.o tablaHash.o generacion.o

all: ${BIN}

alfa: y.tab.o lex.yy.o compilador.o $(OBJ)
	$(CC) -o $(ALFA) $^

lex.yy.o: lex.yy.c
	$(CC) ${CYYFLAGS} -c -o $@ $<

y.tab.o: y.tab.c
	$(CC) ${CYYFLAGS} -c -o $@ $<

%.o: %.c
	$(CC) ${CFLAGS} -c -o $@ $<

y.tab.c: alfa.y
	$(BISON) $(BISONFLAGS) alfa.y

y.tab.h: alfa.y
	$(BISON) $(BISONFLAGS) alfa.y

lex.yy.c: alfa.l y.tab.h
	$(FLEX) $(FLEXFLAGS) alfa.l

clean:
	@rm -rvf $(BIN) $(OBJ) lex.yy.c lex.yy.o y.tab.h y.tab.c y.tab.o y.output alfa
