package es.uam.eps.dadm.cardsjavier.database

import androidx.lifecycle.LiveData
import androidx.room.*
import es.uam.eps.dadm.cardsjavier.deck.DeckWithCards
import es.uam.eps.dadm.cardsjavier.card.Card
import es.uam.eps.dadm.cardsjavier.deck.Deck

@Dao
interface CardDao {
    @Query("SELECT * FROM cards_table")
    fun getCards(): LiveData<List<Card>>

    @Query("SELECT * FROM cards_table WHERE id = :id")
    fun getCard(id: String): LiveData<Card?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCard(card: Card)

    @Update
    fun updateCard(card: Card)

    @Delete
    fun deleteCard(card: Card)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDeck(deck: Deck)

    @Transaction
    @Query("SELECT * FROM decks_table where userId = :userId")
    fun getDecksWithCards(userId: String): LiveData<List<DeckWithCards>>

    @Transaction
    @Query("SELECT * FROM decks_table where userId = :userId")
    fun getDecksWithCardsForSync(userId: String): List<DeckWithCards>

    @Transaction
    @Query("SELECT * FROM decks_table where deckId= :deckId")
    fun getDeckWithCards(deckId: Long): LiveData<DeckWithCards>

    @Transaction
    @Update
    fun updateDeckWithCards(deck: Deck)

    @Transaction
    @Delete
    fun deleteDeckWithCards(deck: Deck)


}