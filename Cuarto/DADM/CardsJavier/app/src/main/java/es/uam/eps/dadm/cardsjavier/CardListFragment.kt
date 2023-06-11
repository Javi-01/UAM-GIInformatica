package es.uam.eps.dadm.cardsjavier

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import es.uam.eps.dadm.cardsjavier.card.Card
import es.uam.eps.dadm.cardsjavier.card.CardAdapter
import es.uam.eps.dadm.cardsjavier.card.CardListViewModel
import es.uam.eps.dadm.cardsjavier.database.CardDatabase
import es.uam.eps.dadm.cardsjavier.databinding.FragmentCardListBinding
import java.util.concurrent.Executors

class CardListFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()

    private lateinit var binding: FragmentCardListBinding
    private lateinit var adapter: CardAdapter

    private val cardListViewModel by lazy {
        ViewModelProvider(this)[CardListViewModel::class.java]
    }

    private var deckId: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCardListBinding.inflate(inflater, container, false)

        val args = CardListFragmentArgs.fromBundle(requireArguments())
        deckId = args.deckId

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CardAdapter(requireActivity())
        adapter.cards = mutableListOf()
        binding.cardListRc.adapter = adapter

        binding.cardAddNew.setOnClickListener {
            val card = Card("", "", deckId = deckId)
            executor.execute {
                CardDatabase.getInstance(requireContext()).cardDao.addCard(card)
            }
            it.findNavController()
                .navigate(CardListFragmentDirections.actionCardListFragmentToCardEditFragment(card.id))
        }

        cardListViewModel.loadDeckId(deckId)

        cardListViewModel.deckWithCards.observe(viewLifecycleOwner) {
            adapter.cards = it.cards.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }


}