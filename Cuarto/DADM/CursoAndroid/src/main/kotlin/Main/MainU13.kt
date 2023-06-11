package Main

fun main() {

    fun esPerfecto(n: Int): Boolean {
        var perfect = 0
        for (i in 1 until n)
            if (n % i == 0) perfect += i

        return perfect == n
    }

    println("Teclea un número entero mayor que 1: ")
    val n = readln().toIntOrNull()

    if (n != null){
        if (esPerfecto(n)){
            println("$n es perfecto")
        }else{
            println("$n no es perfecto")
        }
    }
}
