package Main

import java.lang.Exception

fun leerEntero() : Int {
    print("Introduce un entero entre 1 y 3: ")
    val value = readln().toIntOrNull() ?: throw NumberFormatException("Formato de entero incorrecto")
    if (value !in 1..3) throw Exception("Entero inválido")

    return value
}

fun leerFilaColumna() {
    val fila = leerEntero()
    val columna = leerEntero()
    println("El jugador mueve a ($fila, $columna)")
}

fun main() {
    try {
        leerFilaColumna()
    }
    catch (e: NumberFormatException) {
        println(e.message)
    } catch (e: Exception) {
        println(e.message)
    }
}