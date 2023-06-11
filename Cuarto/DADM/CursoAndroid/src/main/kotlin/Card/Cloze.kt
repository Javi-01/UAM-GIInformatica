package Card

class Cloze(
    question: String,
    answer: String
): Card(question, answer) {
    override fun show() {
        print("$question (INTO para ver la respuesta)")
        readln()
        val buildAnswer = question.split("*")
        print("${buildAnswer[0]}$answer${buildAnswer[2]} (Teclea: 0 -> Difícil; 3 -> Dudo; 5 -> Fácil): ")
        val q = readln().toIntOrNull()
        if (q != null && q in listOf(0, 3, 5)){
            quality = q
        }
    }

    companion object{
        fun fromString(cad: String): Cloze {
            val slices = cad.split(" | ")

            // Constructor
            val cloze = Cloze(question = slices[1], answer = slices[2])

            // Set properties
            cloze.date = slices[3]; cloze.id = slices[4]
            cloze.easiness = slices[5].toDouble(); cloze.repeats = slices[6].toInt(); cloze.interval = slices[7].toLong()
            cloze.quality = slices[8].toInt(); cloze.nextPracticeDate = slices[9]

            return cloze
        }
    }

    override fun toString(): String = "cloze | $question | $answer | $date | $id | $easiness | $repeats | $interval | $quality | $nextPracticeDate\n"

}