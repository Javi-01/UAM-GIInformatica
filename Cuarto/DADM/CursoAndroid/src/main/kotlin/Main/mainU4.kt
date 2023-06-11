package Main

fun main() {
    print("Introduce una calificación entre 0 y 100: ")
    var calificacion = readln().toInt()

    val nota: String = when (calificacion) {
        in 1..49 -> "Suspenso"
        in 50..64 -> "Aprobado"
        in 65..85 -> "Notable"
        in 86..99 -> "Sobresaliente"
        100 -> "Matrícula de honor"
        else -> "Error"
    }

    println("La calificación es $nota")
}