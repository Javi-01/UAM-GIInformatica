package Main

import java.time.LocalDate

fun main() {
    val date = LocalDate.of(2020, 12, 20)

    println("Tu fecha: $date")
    val newDate = date.plusDays(15)

    println("Tu nueva fecha: $newDate")
}