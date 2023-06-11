package Entregables

fun main() {
    println("Telecea tres enteros: ")
    val x = readLine()!!.toIntOrNull()
    val y = readLine()!!.toIntOrNull()
    val z = readLine()!!.toIntOrNull()

    if (x != null && y != null && z != null) {
        if (((x + y) > z) && ((x + z) > y) && ((y + z) > x)){
            println("Es un triángulo")

        }else{
            println("No es un triángulo")

        }
    } else {
        println("Inténtalo otra vez")
    }
}