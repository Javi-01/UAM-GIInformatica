package Card

fun main() {
    val app = App()

    var deckMenu = true
    var deckSelected: Deck? = null

    while (true){

        if (deckMenu){
            println()
            println()
            print("1. Crear mazo\n" +
                    "2. Eliminar mazo\n" +
                    "3. Lista de mazos\n" +
                    "4. Acceder a un mazo\n" +
                    "5. Leer mazos de fichero\n" +
                    "6. Escribir mazos en fichero\n" +
                    "7. Salir\n" +
                    "Elige una opción: "
            )

            when(readln().toIntOrNull()){
                1 -> app.createDeck()
                2 -> app.deleteDeck()
                3 -> app.listDecks()
                4 -> {deckSelected = app.accessDeck(); deckMenu = false}
                5 -> app.readDecks("mazos")
                6 -> app.writeDecks("mazos")
                7 -> break
            }

        }else {
            println()
            println("MENU DEL MAZO '${deckSelected?.name}'")
            println()
            print("1. Añadir tarjeta\n" +
                    "2. Eliminar tarjeta\n" +
                    "3. Lista de tarjetas\n" +
                    "4. Simulación\n" +
                    "5. Leer tarjetas de fichero\n" +
                    "6. Escribir tarjetas en fichero\n" +
                    "7. Volver\n" +
                    "Elige una opción: "
            )

            when(readln().toIntOrNull()){
                1 -> deckSelected?.addCard()
                2 -> deckSelected?.delCard()
                3 -> deckSelected?.listCards()
                4 -> {
                    print("De cuantos días consta la simulación: ")
                    val simDays = readln()
                    if (simDays.toInt() !in 10..20){
                        println("La simulacion debe ser entre 10 y 30 días para estar en los intervalos necesarios")
                    }else{
                        deckSelected?.simulate(simDays.toLong())
                    }
                }
                5 -> deckSelected?.readCards(deckSelected.name)
                6 -> deckSelected?.writeCards(deckSelected.name)
                7 -> {deckSelected = null; deckMenu = true}
            }
        }
    }
}