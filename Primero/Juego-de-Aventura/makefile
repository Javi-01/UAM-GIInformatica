todo: oca space_test set_test die_test inventory_test link_test player_test object_test game_test command_test info clean run
CFLAGS=-Wall -pedantic -g -I $(H)
C = src
H = include
O = obj

oca: $(O)/game_loop.o $(O)/die.o $(O)/set.o $(O)/command.o $(O)/game.o $(O)/graphic_engine.o $(O)/screen.o $(O)/space.o $(O)/gameManagement.o $(O)/object.o $(O)/player.o $(O)/inventory.o $(O)/link.o $(O)/dialogue.o
	gcc $(CFLAGS) $^ -o $@

space_test: $(O)/space_test.o $(O)/space.o $(O)/set.o $(O)/object.o $(O)/link.o
	gcc $(CFLAGS) $^ -o $@
set_test: $(O)/set_test.o $(O)/set.o
	gcc $(CFLAGS) $^ -o $@
die_test: $(O)/die_test.o $(O)/die.o
	gcc $(CFLAGS) $^ -o $@
inventory_test: $(O)/inventory_test.o $(O)/inventory.o $(O)/object.o $(O)/set.o
	gcc $(CFLAGS) $^ -o $@
link_test: $(O)/link_test.o $(O)/link.o
	gcc $(CFLAGS) $^ -o $@
player_test: $(O)/player_test.o $(O)/player.o $(O)/inventory.o $(O)/set.o $(O)/object.o
	gcc $(CFLAGS) $^ -o $@
object_test: $(O)/object_test.o $(O)/object.o $(O)/link.o
	gcc $(CFLAGS) $^ -o $@
command_test: $(O)/command_test.o $(O)/command.o
	gcc $(CFLAGS) $^ -o $@
game_test: $(O)/game_test.o $(O)/game.o $(O)/space.o $(O)/object.o $(O)/link.o $(O)/player.o $(O)/inventory.o $(O)/set.o $(O)/gameManagement.o $(O)/die.o $(O)/dialogue.o
	gcc $(CFLAGS) $^ -o $@

$(O)/game_test.o: $(C)/game_test.c $(H)/game.h $(H)/types.h $(H)/space.h $(H)/set.h $(H)/link.h $(H)/player.h $(H)/game_test.h $(H)/object.h $(H)/gameManagement.h $(H)/die.h $(H)/dialogue.h $(H)/test.h 
		gcc $(CFLAGS) -c src/game_test.c -o obj/game_test.o

$(O)/command_test.o: $(C)/command.c $(C)/command_test.c $(H)/command.h $(H)/command_test.h
		gcc $(CFLAGS) -c src/command_test.c -o obj/command_test.o

$(O)/player_test.o: $(C)/player_test.c $(H)/player.h $(H)/types.h $(H)/space.h $(H)/set.h $(H)/link.h $(H)/inventory.h $(H)/player_test.h $(H)/test.h
	gcc $(CFLAGS) -c src/player_test.c -o obj/player_test.o

$(O)/object_test.o: $(C)/object_test.c $(H)/object.h $(H)/types.h $(H)/object_test.h $(H)/test.h
	gcc $(CFLAGS) -c src/object_test.c  -o obj/object_test.o

$(O)/inventory_test.o: $(C)/inventory_test.c $(H)/inventory.h $(H)/types.h $(H)/set.h $(H)/test.h $(H)/inventory_test.h $(H)/object.h $(H)/space.h $(H)/link.h
	gcc $(CFLAGS) -c src/inventory_test.c -o obj/inventory_test.o

$(O)/link_test.o: $(C)/link_test.c $(H)/link.h $(H)/types.h $(H)/link_test.h $(H)/test.h
	gcc $(CFLAGS) -c src/link_test.c  -o obj/link_test.o

$(O)/die_test.o: $(C)/die_test.c $(H)/die.h $(H)/types.h $(H)/die_test.h $(H)/test.h
	gcc $(CFLAGS) -c src/die_test.c -o obj/die_test.o

$(O)/set_test.o: $(C)/set_test.c $(H)/set.h $(H)/types.h $(H)/test.h $(H)/set_test.h
	gcc $(CFLAGS) -c src/set_test.c -o obj/set_test.o

$(O)/space_test.o: $(C)/space_test.c $(H)/space.h $(H)/types.h $(H)/set.h $(H)/link.h $(H)/space_test.h $(H)/test.h $(H)/object.h
	gcc $(CFLAGS) -c src/space_test.c -o obj/space_test.o

$(O)/game_loop.o: $(C)/game_loop.c $(H)/graphic_engine.h $(H)/game.h $(H)/command.h $(H)/space.h $(H)/types.h $(H)/set.h $(H)/link.h $(H)/player.h $(H)/inventory.h $(H)/object.h $(H)/die.h
	gcc $(CFLAGS) -c src/game_loop.c -o obj/game_loop.o

$(O)/command.o: $(C)/command.c $(H)/command.h
	gcc $(CFLAGS) -c src/command.c -o obj/command.o

$(O)/game.o: $(C)/game.c $(H)/game.h $(H)/command.h $(H)/space.h $(H)/types.h $(H)/set.h $(H)/link.h $(H)/player.h $(H)/inventory.h $(H)/object.h $(H)/die.h $(H)/gameManagement.h $(H)/dialogue.h
	gcc $(CFLAGS) -c src/game.c -o obj/game.o

$(O)/graphic_engine.o: $(C)/graphic_engine.c $(H)/screen.h $(H)/graphic_engine.h $(H)/game.h $(H)/command.h $(H)/space.h $(H)/types.h $(H)/set.h $(H)/link.h $(H)/player.h $(H)/inventory.h $(H)/object.h $(H)/die.h $(H)/gameManagement.h $(H)/dialogue.h
	gcc $(CFLAGS) -c src/graphic_engine.c -o obj/graphic_engine.o

$(O)/screen.o: $(C)/screen.c $(H)/screen.h
	gcc $(CFLAGS) -c src/screen.c -o obj/screen.o

$(O)/space.o: $(C)/space.c $(H)/types.h $(H)/space.h $(H)/set.h $(H)/link.h
	gcc $(CFLAGS) -c src/space.c -o obj/space.o

$(O)/gameManagement.o: $(C)/gameManagement.c $(H)/gameManagement.h $(H)/command.h $(H)/space.h $(H)/types.h $(H)/set.h $(H)/link.h $(H)/game.h $(H)/player.h $(H)/inventory.h $(H)/object.h $(H)/die.h
	gcc $(CFLAGS) -c src/gameManagement.c -o obj/gameManagement.o

$(O)/object.o: $(C)/object.c $(H)/object.h $(H)/space.h $(H)/types.h $(H)/set.h $(H)/link.h
	gcc $(CFLAGS) -c src/object.c -o obj/object.o

$(O)/link.o: $(C)/link.c $(H)/types.h $(H)/link.h
	gcc $(CFLAGS) -c src/link.c -o obj/link.o

$(O)/player.o: $(C)/player.c $(H)/player.h $(H)/types.h $(H)/space.h $(H)/set.h $(H)/link.h $(H)/inventory.h $(H)/object.h $(H)/game.h
	gcc $(CFLAGS) -c src/player.c -o obj/player.o

$(O)/set.o: $(C)/set.c $(H)/object.h $(H)/space.h $(H)/types.h $(H)/set.h $(H)/link.h $(H)/game.h $(H)/command.h $(H)/player.h $(H)/inventory.h $(H)/die.h
	gcc $(CFLAGS) -c src/set.c -o obj/set.o

$(O)/die.o: $(C)/die.c $(H)/die.h $(H)/types.h
	gcc $(CFLAGS) -c src/die.c -o obj/die.o

$(O)/inventory.o: $(C)/inventory.c $(H)/inventory.h $(H)/types.h $(H)/set.h
	gcc $(CFLAGS) -c src/inventory.c -o obj/inventory.o

$(O)/dialogue.o: $(C)/dialogue.c $(H)/dialogue.h $(H)/game.h $(H)/command.h
	gcc $(CFLAGS) -c src/dialogue.c -o obj/dialogue.o

clean:
	rm -f *.h.gch

mv:
	mv *.o obj/
run:
	./oca data.dat -l log.dat

doxy:
	doxygen ./doc/Doxyfile
	rm -r doc/html
	rm -r doc/latex
	mv html ./doc
	mv latex ./doc

info:
	@echo ""
	@echo ""
	@echo ""
	@echo "------------------------------------------------------------------------"
	@echo "Grado en Ingeniería Informática-Universidad Autónoma de Madrid"
	@echo "Proyecto de Programación"
	@echo "Iteración 4"
	@echo "Autores:"
	@echo "-José Miguel Nicolás García"
	@echo "-Iván Fernandez París"
	@echo "-Javier Fraile Iglesias"
	@echo "-Alejandro Jerez Reinoso"
	@echo "------------------------------------------------------------------------"
	@echo ""
	@echo ""
	@echo ""

valgrind:
	valgrind --leak-check=full --track-origins=yes ./oca data.dat -l log.dat

gdb:
	gdb ./oca data.dat -l log.dat
