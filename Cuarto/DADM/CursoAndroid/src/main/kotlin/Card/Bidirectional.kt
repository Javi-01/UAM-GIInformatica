package Card

import kotlin.random.Random

enum class NextAppearance {
    QUESTION, ANSWER
}
class Bidirectional(
    question: String,
    answer: String
): Card(question, answer) {

    private var nextAppearance = NextAppearance.QUESTION

    private fun calculateNextAppearance(currAppearance: NextAppearance){
        NextAppearance.values().filter { NextAppearance.values().contains(currAppearance) }

        nextAppearance = when (quality) {
            // maintain the question in case difficult
            0 -> currAppearance
            // Case 5 flip the question//answer
            5 -> when(currAppearance) {
                NextAppearance.QUESTION -> NextAppearance.ANSWER
                NextAppearance.ANSWER -> NextAppearance.QUESTION
            }
            // Case of 3 or other
            else -> NextAppearance.values().random()
        }
    }
    override fun show() {

        when(nextAppearance) {
            NextAppearance.QUESTION -> {
                val buildQuestion = question.split("*")
                print("${buildQuestion[0]} ___ ${buildQuestion[2]} (INTO para ver la respuesta)")
                readln()
                print("${buildQuestion[1]} (Teclea: 0 -> Difícil; 3 -> Dudo; 5 -> Fácil): ")
            }
            NextAppearance.QUESTION -> {
                val buildAnswer = answer.split("*")
                print("${buildAnswer[0]} ___ ${buildAnswer[2]} (INTO para ver la respuesta)")
                readln()
                print("${buildAnswer[1]} (Teclea: 0 -> Difícil; 3 -> Dudo; 5 -> Fácil): ")
            }
            else -> {}

        }
        val q = readln().toIntOrNull()
        if (q != null && q in listOf(0, 3, 5)){
            quality = q
            calculateNextAppearance(nextAppearance)
        }
    }

    companion object{
        fun fromString(cad: String): Bidirectional {
            val slices = cad.split(" | ")

            // Constructor
            val bidirectional = Bidirectional(question = slices[1], answer = slices[2])

            // Set properties
            bidirectional.date = slices[3]; bidirectional.id = slices[4]
            bidirectional.easiness = slices[5].toDouble(); bidirectional.repeats = slices[6].toInt(); bidirectional.interval = slices[7].toLong()
            bidirectional.quality = slices[8].toInt(); bidirectional.nextPracticeDate = slices[9]; bidirectional.nextAppearance = NextAppearance.valueOf(slices[10])

            return bidirectional
        }
    }

    override fun toString(): String = "bidirectional | $question | $answer | $date | $id | $easiness | $repeats | $interval | $quality | $nextPracticeDate | $nextAppearance\n"

}