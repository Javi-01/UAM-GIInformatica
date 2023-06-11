package es.uam.eps.dadm.cardsjavier

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import es.uam.eps.dadm.cardsjavier.database.CardDatabase
import es.uam.eps.dadm.cardsjavier.databinding.FragmentDeckEditBinding
import es.uam.eps.dadm.cardsjavier.deck.Deck
import es.uam.eps.dadm.cardsjavier.deck.DeckEditViewModel
import java.util.concurrent.Executors


class DeckEditFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()

    private lateinit var binding: FragmentDeckEditBinding
    private lateinit var deck: Deck

    private lateinit var name: String

    private val deckEditViewModel: DeckEditViewModel by lazy {
        ViewModelProvider(this)[DeckEditViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDeckEditBinding.inflate(inflater, container, false)

        val args = DeckEditFragmentArgs.fromBundle(requireArguments())
        deckEditViewModel.loadDeckId(args.deckId)

        deckEditViewModel.deck.observe(viewLifecycleOwner) {
            deck = it.deck
            binding.deck = it.deck
            name = deck.name
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.editItemNameValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                deck.name = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.deckAccept.setOnClickListener {
            if (deck.name.isBlank()) {
                Toast.makeText(activity, "Debes rellenar el campo", Toast.LENGTH_LONG).show()
            }else{
                val cardDatabase = CardDatabase.getInstance(requireContext())
                executor.execute {
                    cardDatabase.cardDao.updateDeckWithCards(deck)
                }
                findNavController().navigate(R.id.action_deckEditFragment_to_deckListFragment)
            }
        }
        binding.deckCancel.setOnClickListener {
            deck.name = name

            if (name.isBlank()) {
                val cardDatabase = CardDatabase.getInstance(requireContext())
                executor.execute {
                    cardDatabase.cardDao.deleteDeckWithCards(deck)
                }
            }

            findNavController().navigate(R.id.action_deckEditFragment_to_deckListFragment)
        }
    }
}