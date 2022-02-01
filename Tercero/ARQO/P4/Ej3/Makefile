LIBS = -lgomp -lm
LIB_DIR =  
FLAGS = -O3 -g -Wall -D_GNU_SOURCE -fopenmp

.PHONY: clean all

all: multiplica_serie multiplica1 multiplica2 multiplica3
 
multiplica_serie: multiplicaMatrices_serie.c arqo3.c
	gcc $(FLAGS) $(LIB_DIR) -o $@ $^ $(LIBS)
 
multiplica1: multiplicaMatrices1.c arqo3.c
	gcc $(FLAGS) $(LIB_DIR) -o $@ $^ $(LIBS)
 
multiplica2: multiplicaMatrices2.c arqo3.c
	gcc $(FLAGS) $(LIB_DIR) -o $@ $^ $(LIBS)
 
multiplica3: multiplicaMatrices3.c arqo3.c
	gcc $(FLAGS) $(LIB_DIR) -o $@ $^ $(LIBS)

clean:
	rm -f *.o *~ multiplica_serie multiplica1 multiplica2 multiplica3
