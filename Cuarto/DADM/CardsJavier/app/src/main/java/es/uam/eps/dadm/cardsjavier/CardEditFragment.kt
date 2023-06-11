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
import es.uam.eps.dadm.cardsjavier.card.Card
import es.uam.eps.dadm.cardsjavier.card.CardEditViewModel
import es.uam.eps.dadm.cardsjavier.database.CardDatabase
import es.uam.eps.dadm.cardsjavier.databinding.FragmentCardEditBinding
import java.util.concurrent.Executors

class CardEditFragment : Fragment() {
    private val executor = Executors.newSingleThreadExecutor()

    private lateinit var binding: FragmentCardEditBinding
    private lateinit var card: Card

    private lateinit var question: String
    private lateinit var answer: String

    private val cardEditViewModel by lazy {
        ViewModelProvider(this)[CardEditViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCardEditBinding.inflate(inflater, container, false)


        val args = CardEditFragmentArgs.fromBundle(requireArguments())
        cardEditViewModel.loadCardId(args.cardId)

        cardEditViewModel.card.observe(viewLifecycleOwner) {
            card = it
            binding.card = it
            question = card.question
            answer = card.answer
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.editItemQuestionValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                card.question = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.editItemAnswerValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                card.answer = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.cardAccept.setOnClickListener {
            if (card.question.isBlank() || card.answer.isBlank()){
                Toast.makeText(activity, "Debes rellenar los campos", Toast.LENGTH_LONG).show()
            }else{
                executor.execute {
                    CardDatabase.getInstance(requireContext()).cardDao.updateCard(card)
                }
                findNavController().
                navigate(CardEditFragmentDirections.actionCardEditFragmentToCardListFragment(card.deckId))
            }
        }
        binding.cardCancel.setOnClickListener {
            card.question = question
            card.answer = answer

            if (answer.isBlank() && question.isBlank()) {
                executor.execute {
                    CardDatabase.getInstance(requireContext()).cardDao.deleteCard(card)
                }
            }
            findNavController().
            navigate(CardEditFragmentDirections.actionCardEditFragmentToCardListFragment(card.deckId))
        }
    }
}