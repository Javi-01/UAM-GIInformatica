package Main

import java.time.LocalTime

fun main() {
    val actualTime = LocalTime.now()

    println("Momento actual: $actualTime")
    val newTime = actualTime.plusHours(1).plusMinutes(20)

    println("Nuevo momento: $newTime")
}