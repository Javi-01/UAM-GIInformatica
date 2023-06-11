package Entregables

fun main() {

    println("Teclea dos enteros: ")
    val op1 = readln().toIntOrNull()
    val op2 = readln().toIntOrNull()

    println("1. Suma\n" +
            "2. Resta\n" +
            "3. Multiplicación\n" +
            "4. Resto división entera"
    )

    val operando = readln()

    val operacion: (Int, Int) -> Int = { o1, o2 ->
        when(operando.toIntOrNull()){
            1 -> o1 + o2
            2 -> o1 - o2
            3 -> o1 * o2
            4 -> o1 / o2
            else -> -1
        }
    }

    fun operar(a: Int, b: Int, op: (Int, Int) -> Int): Int{
        return op(a, b)
    }

    if (op1 != null && op2!= null) println(operar(op1,op2, operacion))
}