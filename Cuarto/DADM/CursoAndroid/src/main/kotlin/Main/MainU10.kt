package Main

fun main() {
    val andalucia = mutableListOf("Almería", "Córdoba", "Granada", "Málaga", "Sevilla")

    andalucia.addAll(listOf("Cádiz", "Jaén", "Huelva"))
    andalucia.sort()

    println(andalucia)
}