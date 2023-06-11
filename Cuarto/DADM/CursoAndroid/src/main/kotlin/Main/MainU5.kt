package Main

fun main() {
    print("Introduce una calificación entre 0 y 100: ")
    var calificacion = readln().toInt()

    val nota: String = calculaCalificacion(calificacion)
    println("La calificación es $nota")

    if (aprueba(calificacion))
        println("Has superado la prueba")
    else
        println("Debes volver a intentarlo")
}


private fun calculaCalificacion(qualification: Int): String = when (qualification) {
        in 1..49 -> "Suspenso"
        in 50..64 -> "Aprobado"
        in 65..85 -> "Notable"
        in 86..99 -> "Sobresaliente"
        100 -> "Matrícula de honor"
        else -> "Error"
    }

private fun aprueba(qualification: Int, umbral: Double = 50.0): Boolean = qualification >= umbral