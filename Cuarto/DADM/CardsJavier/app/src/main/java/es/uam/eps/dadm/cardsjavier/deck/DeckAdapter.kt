package es.uam.eps.dadm.cardsjavier.deck

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import es.uam.eps.dadm.cardsjavier.DeckListFragmentDirections
import es.uam.eps.dadm.cardsjavier.database.CardDatabase
import es.uam.eps.dadm.cardsjavier.databinding.ListItemDeckBinding
import java.util.concurrent.Executors

class DeckAdapter : RecyclerView.Adapter<DeckAdapter.DeckHolder>(){
    private val executor = Executors.newSingleThreadExecutor()

    var decks = mutableListOf<DeckWithCards>()
    private lateinit var binding: ListItemDeckBinding

    inner class DeckHolder(view: View): RecyclerView.ViewHolder(view){
        private var local = binding
        fun bind(deck: DeckWithCards, context: Context) {
            local.deckCards = deck
            binding.listItemDeleteDeck.setOnClickListener {
                notifyItemRemoved(decks.indexOf(deck))
                val cardDatabase = CardDatabase.getInstance(context)
                executor.execute {
                    cardDatabase.cardDao.deleteDeckWithCards(deck.deck)
                }
                decks.remove(deck)
            }
            binding.listItemEditDeck.setOnClickListener {
                it.findNavController()
                    .navigate(DeckListFragmentDirections.actionDeckListFragmentToDeckEditFragment(deck.deck.deckId))
            }
            binding.root.setOnClickListener {
                it.findNavController()
                    .navigate(DeckListFragmentDirections.actionDeckListFragmentToCardListFragment(deck.deck.deckId))
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckHolder {
        val  layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemDeckBinding.inflate(layoutInflater, parent, false)

        return DeckHolder(binding.root)
    }

    override fun getItemCount(): Int = decks.size

    override fun onBindViewHolder(holder: DeckHolder, position: Int) {
        holder.bind(decks[position], holder.itemView.context)
    }

}