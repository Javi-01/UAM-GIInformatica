package es.uam.eps.dadm.cardsjavier.deck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelStoreOwner
import es.uam.eps.dadm.cardsjavier.card.StatisticsViewModel
import es.uam.eps.dadm.cardsjavier.databinding.ListItemStatsDeckBinding

class DeckStatsAdapter(activity: ViewModelStoreOwner) : RecyclerView.Adapter<DeckStatsAdapter.DeckHolder>(){

    var decks = listOf<DeckWithCards>()
    private lateinit var binding: ListItemStatsDeckBinding
    private val statsViewModel: StatisticsViewModel by lazy {
        ViewModelProvider(activity)[StatisticsViewModel::class.java]
    }

    inner class DeckHolder(view: View): RecyclerView.ViewHolder(view){
        private var local = binding
        fun bind(deckWithCards: DeckWithCards) {
            local.deckWithCards = deckWithCards

            binding.root.setOnClickListener {
                // Establecer el nuevo deck para mostrar sus estadisticas
                statsViewModel.loadStatisticsDeckId(deckWithCards.deck.deckId)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckHolder {
        val  layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemStatsDeckBinding.inflate(layoutInflater, parent, false)

        return DeckHolder(binding.root)
    }

    override fun getItemCount(): Int = decks.size

    override fun onBindViewHolder(holder: DeckHolder, position: Int) {
        holder.bind(decks[position])
    }

}