LIBS = 
LIB_DIR =  
FLAGS = -O3 -g -Wall -D_GNU_SOURCE

.PHONY: clean all

all: fast slow multiplica traspuesta

fast: fast.c arqo3.c
	gcc $(FLAGS) $(LIB_DIR) -o $@ $^ $(LIBS)

slow: slow.c arqo3.c
	gcc $(FLAGS) $(LIB_DIR) -o $@ $^ $(LIBS)
 
multiplica: multiplicaMatrices.c arqo3.c
	gcc $(FLAGS) $(LIB_DIR) -o $@ $^ $(LIBS)

traspuesta: traspuestaMatriz.c arqo3.c
	gcc $(FLAGS) $(LIB_DIR) -o $@ $^ $(LIBS)

clean:
	rm -f *.o *~ fast slow multiplica traspuesta
