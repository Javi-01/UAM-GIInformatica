from distutils.log import error
import sys

def Celsius_to_Farenheit(temp):
    return str(float((temp * 9 / 5) + 32))

if len(sys.argv) == 2:
    try:
        temp = float(sys.argv[1])
        print(str(temp) + " grados Celsius son " + Celsius_to_Farenheit(temp) + " grados Farenheit\n")
    except ValueError:
        print("El parámetro introducido debe ser un número para poder ser convertido\n")
else:
    print("Error - Introduce unicamente 1 argumento\n")