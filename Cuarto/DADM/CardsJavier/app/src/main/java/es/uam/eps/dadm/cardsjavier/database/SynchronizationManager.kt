package es.uam.eps.dadm.cardsjavier.database

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.uam.eps.dadm.cardsjavier.card.Card
import es.uam.eps.dadm.cardsjavier.deck.DeckListViewModel
import es.uam.eps.dadm.cardsjavier.deck.DeckWithCards
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class SynchronizationManager(private val context: Context) {
    private val executor = Executors.newSingleThreadExecutor()


    // Download Firebase Data to Local
    fun fireBaseToRoomSynchronization() {
        val reference = FirebaseDatabase.getInstance().getReference("users")
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        reference.child(currentUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    executor.execute {
                      for (snapCard in snapshot.children) {
                        // Obtener el deckWithCards y Añadir primero el deck y luego las cards
                            snapCard.getValue(DeckWithCards::class.java)?.let {
                                CardDatabase.getInstance(context).cardDao.addDeck(it.deck)
                                for (card in it.cards){
                                    CardDatabase.getInstance(context).cardDao.addCard(card)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    // Upload Local Database to Firebase
    fun roomToFirebaseSynchronization() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        executor.execute {
            val decks =
                CardDatabase.getInstance(context).cardDao.getDecksWithCardsForSync(currentUserId)

            if (decks.isNotEmpty()) {
                val reference = FirebaseDatabase.getInstance().getReference("users")
                decks.forEachIndexed { index, deckWithCards ->
                    reference.child(currentUserId).child("DeckWithCards${index}").setValue(deckWithCards)
                }
            }
        }
    }
}
