from distutils.log import error
import sys

def saludar(nombre):
    return f"Hola " + nombre + "!\n"

if len(sys.argv) == 2:
    nombre = str(sys.argv[1])
    print(saludar(nombre))
else:
    print("Error - Introduce unicamente 1 argumento\n")