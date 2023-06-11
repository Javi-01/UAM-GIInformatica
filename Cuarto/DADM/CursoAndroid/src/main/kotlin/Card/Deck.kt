package Card

import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.lang.NullPointerException
import java.time.LocalDateTime
import java.util.*

class Deck(
    var name: String,
    var id: String = UUID.randomUUID().toString(),
){
    private var cards: MutableList<Card> = mutableListOf()

    private fun addCardContext(bidirectional: Boolean = false): List<String> {
        val question: String
        val answer: String
        if (bidirectional){
            print("Teclea una frase donde este comprendida la PREGUNTA entre (*)\n" +
                    "Ej: La capital de *Alemania* es Berlín: ")
            answer = readln()
            print("Teclea una frase donde este comprendida la RESPUESTA entre (*)\n" +
                    "Ej: *Berlín* es la capital de Alemania: ")
            question = readln()

        }else{
            print("Teclea una pregunta: ")
            question = readln()
            print("Teclea una respuesta: ")
            answer = readln()
        }

        if (question.isBlank() || answer.isBlank()){
            println("La tarjeta no es valida")
        }else{
            return listOf(question, answer)
        }
        return listOf()
    }

    fun addCard() {
        println("Añade una tarjeta al mazo $name")
        print("Teclea el tipo (0 -> Card, 1 -> Cloze, 2 -> Bidirectional): ")

        when(readln()){
            "0" -> {
                val context = addCardContext()
                if (context.size == 2){
                    cards.add(Card(context[0], context[1]))
                    println("Tarjeta añadida correctamente")
                }
            }
            "1" -> {
                val context = addCardContext()
                if (context.size == 2){
                    cards.add(Cloze(context[0], context[1]))
                    println("Tarjeta añadida correctamente")
                }
            }
            "2" -> {
                val context = addCardContext(true)
                if (context.size == 2) {
                   cards.add(Bidirectional(context[0], context[1]))
                    println("Tarjeta añadida correctamente")
                }
            }
            else -> println("Opcion inválida")
        }
    }

    fun delCard() {
        if (cards.size > 0){
            cards.forEachIndexed { index, card ->
                println("$index -> ${card.question}")
            }
            print("Selecciona la tarjeta que desea eliminar por su numero: ")

            val cardIdxToDelete = readln()
            if (cardIdxToDelete.toInt() !in cards.indices){
                println("Debes seleccionar una carta disponible")
            }else{
                cards.removeAt(cardIdxToDelete.toInt())
            }
        }else {
            println("Ninguna carta que poder borrar")
        }

    }
    fun listCards() {
        cards.forEach {
            println("${it.question} -> ${it.answer}")
        }
    }

    fun writeCards(name: String) {
        if (cards.isEmpty()) {
            try{
                val file = File("data/$name.txt")
                // Clean the file info
                RandomAccessFile(file, "rw").apply {
                    setLength(0)
                    close()
                }

                // Write the cards
                cards.forEach { file.appendText(it.toString()) }

            }catch (e: NullPointerException) {
                println("Fichero sin nombre no se puede crear")
            }
        }
    }

    fun readCards(name: String) {
        try{
            val fileLines = File("data/$name.txt").readLines()

            fileLines.forEach {
                // Get the type of card
                when(it.split(" | ")[0]) {
                    "card" -> cards.add(Card.fromString(it))
                    "cloze" -> cards.add(Cloze.fromString(it))
                    "bidirectional" -> cards.add(Bidirectional.fromString(it))
                }
            }

        }catch (e: FileNotFoundException) {
            println("Fichero incorreto, intentalo de nuevo")
        }
    }
    fun simulate(period: Long){
        println("Simulacion del mazo $name")

        var now = LocalDateTime.now()
        for (i in 1 until period){
            cards.forEach {
                it.simulate(now)
            }
            now = now.plusDays(1)
        }
    }

    companion object{
        fun fromString(cad: String): Deck {
            val slices = cad.split(" | ")

            // Constructor
            return Deck(name = slices[1], id = slices[2])
        }
    }

    override fun toString(): String = "deck | $name | $id\n"

}