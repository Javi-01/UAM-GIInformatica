CC = gcc
LIBS = -lgomp -lm
LIB_DIR =  
FLAGS = -g -Wall -D_GNU_SOURCE -fopenmp

.PHONY: clean all

all: edgeDetectorSerie edgeDetectorA2 edgeDetectorA3B1 edgeDetectorA3B2 edgeDetectorA3B3 clear
 
edgeDetectorSerie: edgeDetectorSerie.c stb_image_write.h stb_image.h
	$(CC) $(FLAGS) -std=gnu99 $(LIB_DIR) -o $@ edgeDetectorSerie.c $(LIBS)
 
edgeDetectorA2: edgeDetectorA2.c stb_image_write.h stb_image.h
	$(CC) $(FLAGS) -std=gnu99 $(LIB_DIR) -o $@ edgeDetectorA2.c $(LIBS)
 
edgeDetectorA3B1: edgeDetectorA3B1.c stb_image_write.h stb_image.h
	$(CC) $(FLAGS) -std=gnu99 $(LIB_DIR) -o $@ edgeDetectorA3B1.c $(LIBS)

edgeDetectorA3B2: edgeDetectorA3B2.c stb_image_write.h stb_image.h
	$(CC) $(FLAGS) -std=gnu99 $(LIB_DIR) -o $@ edgeDetectorA3B2.c $(LIBS)
 
edgeDetectorA3B3: edgeDetectorA3B3.c stb_image_write.h stb_image.h
	$(CC) $(FLAGS) -std=gnu99 $(LIB_DIR) -o $@ edgeDetectorA3B3.c $(LIBS)

stb_image.h:
	@echo "Los ficheros de cabecera de stb se deberían descarga automáticamente"
	@echo "En caso de fallo, descargadlo de https://raw.githubusercontent.com/nothings/stb/master/stb_image.h"
	wget --no-check-certificate https://raw.githubusercontent.com/nothings/stb/master/stb_image.h

stb_image_write.h:
	@echo "Los ficheros de cabecera de stb se deberían descarga automáticamente"
	@echo "En caso de fallo, descargadlo de https://raw.githubusercontent.com/nothings/stb/master/stb_image_write.h"
	wget --no-check-certificate https://raw.githubusercontent.com/nothings/stb/master/stb_image_write.h

clean:
	rm -f *.o *~ edgeDetectorSerie edgeDetectorA2 edgeDetectorA3B1 edgeDetectorA3B2 edgeDetectorA3B3
	
clear:
	rm -f *.o *~
