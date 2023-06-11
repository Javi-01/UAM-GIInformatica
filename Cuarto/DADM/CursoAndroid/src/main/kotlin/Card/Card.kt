package Card

import java.time.LocalDateTime
import java.util.UUID
import kotlin.math.roundToLong


open class Card (
    var question: String,
    var answer: String,
    var date: String = LocalDateTime.now().toString(),
    var id: String = UUID.randomUUID().toString(),

    ){
    var quality: Int = 0
    var repeats: Int = 0
    var interval: Long = 1L
    var nextPracticeDate: String = date
    var easiness: Double = 2.5

    open fun show() {
        print("$question (INTO para ver la respuesta)")
        readln()
        print("$answer (Teclea: 0 -> Difícil; 3 -> Dudo; 5 -> Fácil): ")
        val q = readln().toIntOrNull()
        if (q != null && q in listOf(0, 3, 5)){
            quality = q
        }
    }

    private fun update(currentDate: LocalDateTime) {
        // Update the easiness of the question
        easiness = maxOf(
            1.3,
            easiness + 0.1 - (5-quality) * (0.08 + (5-quality) * 0.02)
        )

        // Update the repeats of the question (0 if hard, else r + 1)
        repeats = if (quality < 3) 0 else repeats + 1

        // Update the interval of the question
        interval = if (repeats <= 1) 1 else if (repeats == 2) 6 else (interval * easiness).roundToLong()

        // Update the nextPractice
        nextPracticeDate = currentDate.plusDays(interval).toString()
    }

    private fun details(){
        val next = LocalDateTime.parse(nextPracticeDate)
        println("eas = ${String.format("%.2f", easiness)}, rep = $repeats, int = $interval, " +
                "next = ${next.year}-${next.monthValue}-${next.dayOfMonth}")
    }

    fun simulate(now: LocalDateTime){
        println("Simulacion de tarjeta: $question")

        println("Fecha actual: ${now.year}-${now.monthValue}-${now.dayOfMonth}")
        if (now.dayOfYear == LocalDateTime.parse(nextPracticeDate).dayOfYear){
            show()
            update(now)
            details()
        }
    }

    companion object{
        fun fromString(cad: String): Card {
            val slices = cad.split(" | ")
            // Constructor
            val card = Card(question = slices[1], answer = slices[2], date = slices[3], id = slices[4])

            //Set the properties
            card.easiness = slices[5].toDouble(); card.repeats = slices[6].toInt(); card.interval = slices[7].toLong()
            card.quality = slices[8].toInt(); card.nextPracticeDate = slices[9]

            return card
        }
    }

    override fun toString(): String = "card | $question | $answer | $date | $id | $easiness | $repeats | $interval | $quality | $nextPracticeDate\n"
}