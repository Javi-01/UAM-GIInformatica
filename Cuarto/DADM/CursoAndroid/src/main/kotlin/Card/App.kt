package Card

import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.lang.NullPointerException

class App {
    private var decks: MutableList<Deck> = mutableListOf()
    fun createDeck() {
        print("Nombre del mazo: ")
        val name = readln()

        if (decks.count { it.name == name } > 0){
            println("Nombre ya esta en la lista de mazos creados")
        }else{
            decks.add(Deck(name = name))
        }
    }

    fun deleteDeck() {
        if (decks.size > 0) {
            decks.forEachIndexed { index, deck ->
                println("$index -> ${deck.name}")
            }
            print("Selecciona el mazo que desea eliminar por su numero: ")

            val deckIdxToDelete = readln()
            if (deckIdxToDelete.toInt() !in decks.indices){
                println("Debes seleccionar un mazo disponible")
            }else{
                decks.removeAt(deckIdxToDelete.toInt())
            }
        }else {
            println("Ningun mazo que borrar")
        }

    }

    fun listDecks() {
        decks.forEach {
            println(it.name)
        }
    }

    fun accessDeck(): Deck? {
        decks.forEachIndexed { index, deck ->
            println("$index -> ${deck.name}")
        }
        print("Selecciona un mazo al que acceder: ")
        val deckIdxToAccess = readln().toIntOrNull()
        if (deckIdxToAccess !in decks.indices){
            println("Debes seleccionar un mazo disponible")
        }else{
            return decks[deckIdxToAccess!!]
        }
        return null
    }

    fun readDecks(name: String) {
        try{
            val fileLines = File("data/$name.txt").readLines()
            fileLines.forEach { decks.add(Deck.fromString(it)) }

        }catch (e: FileNotFoundException) {
            println("Fichero incorreto, intentalo de nuevo")
        }
    }

    fun writeDecks(name: String) {
        if (decks.isNotEmpty()){
            try{
                val file = File("data/$name.txt")
                // Clean the file info
                RandomAccessFile(file, "rw").apply {
                    setLength(0)
                    close()
                }

                // Write the deck
                decks.forEach { file.appendText(it.toString()) }

            }catch (e: NullPointerException) {
                println("Fichero sin nombre no se puede crear")
            }
        }
    }
}