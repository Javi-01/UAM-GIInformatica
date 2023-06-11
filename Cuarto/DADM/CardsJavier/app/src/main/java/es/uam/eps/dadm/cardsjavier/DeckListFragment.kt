package es.uam.eps.dadm.cardsjavier

import android.annotation.SuppressLint
import android.media.audiofx.Equalizer.Settings
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cardsjavier.database.CardDatabase
import es.uam.eps.dadm.cardsjavier.databinding.FragmentDeckListBinding
import es.uam.eps.dadm.cardsjavier.deck.Deck
import es.uam.eps.dadm.cardsjavier.deck.DeckAdapter
import es.uam.eps.dadm.cardsjavier.deck.DeckListViewModel
import java.util.concurrent.Executors


class DeckListFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()

    private lateinit var binding: FragmentDeckListBinding
    private lateinit var adapter: DeckAdapter

    private lateinit var auth: FirebaseAuth
    private val deckListViewModel: DeckListViewModel by lazy {
        ViewModelProvider(this)[DeckListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDeckListBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DeckAdapter()
        adapter.decks = mutableListOf()
        binding.deckListRc.adapter = adapter

        binding.deckAddNew.setOnClickListener {
            val deck = Deck(name = "", userId = auth.uid ?: "")
            executor.execute {
                val cardDatabase = CardDatabase.getInstance(requireContext())
                cardDatabase.cardDao.addDeck(deck)
            }

            it.findNavController()
                .navigate(DeckListFragmentDirections.actionDeckListFragmentToDeckEditFragment(deck.deckId))
        }

        deckListViewModel.loadUserId(auth.uid!!)

        deckListViewModel.decks.observe(viewLifecycleOwner) {
            adapter.decks = it.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }
}