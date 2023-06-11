package Entregables

fun main() {
    println("Telecea un numero entero: ")
    val num = readLine()!!.toIntOrNull()

    if (num != null){
        if (num%3==0 && num%5==0) println("$num es divisible por 3 y 5")
        else println("$num no es divisible por 3 y 5")
    }else{
        println("Inténtalo otra vez")
    }
}