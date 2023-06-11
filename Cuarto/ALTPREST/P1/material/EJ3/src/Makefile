CC = gcc
LIBS = -lm
LIB_DIR =
FLAGS = -O3 -march=native -fwhole-program -Wall -D_GNU_SOURCE

.PHONY: clean all

all: greyScale greyScale_auto-vectorized greyScale_intrinsic clear

greyScale: greyScale.c stb_image_write.h stb_image.h
	$(CC) $(FLAGS) -std=gnu99 $(LIB_DIR) -o $@ greyScale.c $(LIBS)

greyScale_auto-vectorized: greyScale_auto-vectorized.c stb_image_write.h stb_image.h
	$(CC) $(FLAGS) -std=gnu99 $(LIB_DIR) -o $@ greyScale_auto-vectorized.c $(LIBS)

greyScale_intrinsic: greyScale_intrinsic.c stb_image_write.h stb_image.h
	$(CC) $(FLAGS) -std=gnu99 $(LIB_DIR) -o $@ greyScale_intrinsic.c $(LIBS)


stb_image.h:
	@echo "Los ficheros de cabecera de stb se deberían descarga automáticamente"
	@echo "En caso de fallo, descargadlo de https://raw.githubusercontent.com/nothings/stb/master/stb_image.h"
	wget --no-check-certificate https://raw.githubusercontent.com/nothings/stb/master/stb_image.h

stb_image_write.h:
	@echo "Los ficheros de cabecera de stb se deberían descarga automáticamente"
	@echo "En caso de fallo, descargadlo de https://raw.githubusercontent.com/nothings/stb/master/stb_image_write.h"
	wget --no-check-certificate https://raw.githubusercontent.com/nothings/stb/master/stb_image_write.h

clean:
	rm -f *.o *~ greyScale greyScale_auto-vectorized greyScale_intrinsic

clear:
	rm -f *.o *~
