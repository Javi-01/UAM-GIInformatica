package es.uam.eps.dadm.cardsjavier.card

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import es.uam.eps.dadm.cardsjavier.database.CardDatabase
import es.uam.eps.dadm.cardsjavier.CardListFragmentDirections
import es.uam.eps.dadm.cardsjavier.databinding.ListItemCardBinding
import java.util.concurrent.Executors

class CardAdapter(activity: ViewModelStoreOwner) : RecyclerView.Adapter<CardAdapter.CardHolder>(){
    private val executor = Executors.newSingleThreadExecutor()

    var cards = mutableListOf<Card>()
    private lateinit var binding: ListItemCardBinding

    inner class CardHolder(view: View): RecyclerView.ViewHolder(view){
        private var local = binding
        fun bind(card: Card, context: Context) {
            local.card = card
            local.listItemExpandMore.setOnClickListener {
                local.listItemExpandMore.visibility = View.GONE
                local.listItemExpandLess.visibility = View.VISIBLE
                local.listItemEasinessTag.visibility = View.VISIBLE
                local.listItemEasiness.visibility = View.VISIBLE
                local.listItemRepetitionsTag.visibility = View.VISIBLE
                local.listItemRepetitions.visibility = View.VISIBLE
                local.listItemNextPracticeTag.visibility = View.VISIBLE
                local.listItemNextPractice.visibility = View.VISIBLE
            }

            local.listItemExpandLess.setOnClickListener {
                local.listItemExpandLess.visibility = View.GONE
                local.listItemExpandMore.visibility = View.VISIBLE
                local.listItemEasinessTag.visibility = View.GONE
                local.listItemEasiness.visibility = View.GONE
                local.listItemRepetitionsTag.visibility = View.GONE
                local.listItemRepetitions.visibility = View.GONE
                local.listItemNextPracticeTag.visibility = View.GONE
                local.listItemNextPractice.visibility = View.GONE
            }
            local.listDeleteCard.setOnClickListener {
                notifyItemRemoved(cards.indexOf(card))
                val cardDatabase = CardDatabase.getInstance(context)
                executor.execute {
                    cardDatabase.cardDao.deleteCard(card)
                }
                cards.remove(card)
            }

            binding.root.setOnClickListener {
                it.findNavController()
                    .navigate(CardListFragmentDirections.actionCardListFragmentToCardEditFragment(card.id))

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val  layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemCardBinding.inflate(layoutInflater, parent, false)

        return CardHolder(binding.root)
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(cards[position], holder.itemView.context)
    }

}