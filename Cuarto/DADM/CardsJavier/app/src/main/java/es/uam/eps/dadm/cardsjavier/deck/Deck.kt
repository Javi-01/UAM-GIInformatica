package es.uam.eps.dadm.cardsjavier.deck

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "decks_table")
class Deck(
    @PrimaryKey var deckId: Long = generateUUID(),
    var name: String,
    var userId: String
){
    constructor() : this(generateUUID(), "", "")

}


private fun generateUUID(): Long {
    val id = UUID.randomUUID()
    return id.mostSignificantBits xor id.leastSignificantBits
}




