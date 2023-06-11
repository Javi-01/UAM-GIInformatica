package es.uam.eps.dadm.cardsjavier.deck

import androidx.room.Embedded
import androidx.room.Relation
import es.uam.eps.dadm.cardsjavier.card.Card

data class DeckWithCards(
    @Embedded
    val deck: Deck,
    @Relation(
        parentColumn = "deckId",
        entityColumn = "deckId"
    )
    val cards: List<Card>,
){
    constructor() : this(Deck(0, "", ""), emptyList())

}