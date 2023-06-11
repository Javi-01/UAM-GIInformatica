package Codigo

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

fun miliseconds() {
    val start = System.currentTimeMillis()

    for (i in 1..1000000){}
    println("El timepo invertido son ${System.currentTimeMillis() - start}  milisegundos")
}

fun dateCompare(){
    val dateStart = Date()

    for (i in 1..1000000){}
    println("Comparacion de fechas: ${dateStart.compareTo(Date())}")
    // CompareTo -> -1 si ini es > fin
    // CompareTo -> 1 si ini es < fin
    // CompareTo -> 0 si ini es == fin
}

fun dateFormat() {
    val date = Date()
    // Formato de fecha
    val format = SimpleDateFormat("dd-MM-yyyy HH:mm")
    // Cadena de caracteres a formato
    val otherDate: Date = format.parse("10-02-2023 11:04")

    println("$date formateada a ${format.format(date)}")
    println(format.format(otherDate))
}

fun calendar() {
    val calendario = GregorianCalendar()
    val format = SimpleDateFormat("dd-MM-yyyy HH:mm")

    calendario.set(Calendar.DAY_OF_MONTH, 12)
    calendario.set(Calendar.MONTH, 0)
    calendario.set(Calendar.YEAR, 2023)

    calendario.add(Calendar.DAY_OF_WEEK,1)
    println(format.format(calendario.time))
}

fun main() {

    val time = LocalTime.now()
    val dateTime = LocalDateTime.now()
    val zoneDateTime = ZonedDateTime.now()

    println(time)
    println(dateTime)
    println(zoneDateTime)

}