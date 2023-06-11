package es.uam.eps.dadm.cardsjavier.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID
import kotlin.math.roundToLong


@Entity(tableName = "cards_table")
open class Card (
    @ColumnInfo(name = "card_question") var question: String,
    var answer: String,
    var date: String = LocalDateTime.now().toString(),
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var deckId: Long = 0

    ){
    var answered: Boolean = false

    var quality: Int = 0
    var repeats: Int = 0
    var interval: Long = 1L
    var nextPracticeDate: String = date
    var easiness: Double = 2.5

    constructor() : this(
        "question",
        "answer",
        LocalDateTime.now().toString(),
        UUID.randomUUID().toString(),
        0
    )
    fun isDue(date: LocalDateTime): Boolean =
        LocalDateTime.parse(nextPracticeDate) <= date

    fun quality(): String {
        return when(quality){
            0 -> "Dificil"
            3 -> "Duda"
            5 -> "Facil"
            else -> ""
        }
    }

    fun updateCard(q: Int) {
        quality = q
        update()
    }

    private fun update() {
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
        nextPracticeDate = LocalDateTime.now().plusDays(interval).toString()
    }


}