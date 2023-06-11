package Entregables

fun main() {

    println("Teclea un entero: ")
    val entero = readln().toIntOrNull()

    if (entero != null){
        println("Los divisores de $entero son: ")
        for (i in 1 until entero){
            if (entero % i == 0){
                println("$i ")
            }
        }
    }else{
        println("Intentalo otra vez")
    }
}