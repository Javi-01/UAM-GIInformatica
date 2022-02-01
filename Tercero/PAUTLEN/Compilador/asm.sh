# Tests para el examen
./alfa pruebas/ej1.alfa ej1.asm
nasm -g -felf32 ej1.asm
gcc -m32 -o ej1 -g alfalib.o ej1.o
