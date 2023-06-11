package Main

fun main() {

    val mayorDeEdad: (Int) -> Boolean = fun(edad):Boolean {return edad >= 18}

    println("Teclea tu edad: ")
    val edad = readLine()!!.toIntOrNull()

    if (edad != null)
        if (mayorDeEdad(edad)) println("Eres mayor de edad")
        else println("No eres mayor de edad")
}