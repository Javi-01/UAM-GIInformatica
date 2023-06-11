package Main

fun main() {
    print("Por favor, teclea la base del triángulo: ")
    val base = readLine()!!.toDoubleOrNull()

    print("Por favor, teclea la altura del triángulo: ")
    val altura = readLine()!!.toDoubleOrNull()


    if (base != null && altura!= null){
        println("El área del triángulo es ${(base*altura)/ 2}")
    }else{
        println("Por favor, vuelve a intentarlo")
    }
}