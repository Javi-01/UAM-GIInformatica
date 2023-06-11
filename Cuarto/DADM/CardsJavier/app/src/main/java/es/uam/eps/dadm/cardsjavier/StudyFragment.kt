package es.uam.eps.dadm.cardsjavier

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import es.uam.eps.dadm.cardsjavier.board.BoardView
import es.uam.eps.dadm.cardsjavier.card.StudyViewModel
import es.uam.eps.dadm.cardsjavier.databinding.FragmentStudyBinding


class StudyFragment : Fragment(){

    private lateinit var binding: FragmentStudyBinding
    // La propiedad Lazy permite que solo se ejecute una vez cuando
    // se crea el objeto, de esta manera el view model no se tiene
    // que llamar cada vez que se crea la actividad, ya que el view
    //model se mantiene en memoria
    private val studyViewModel: StudyViewModel by lazy {
        ViewModelProvider(this)[StudyViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (SettingsActivity.getBoardView(requireContext())) {
            val boardView = BoardView(requireContext())
            binding.boardView.addView(boardView)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentStudyBinding.inflate(inflater, container, false)

        binding.viewModel = studyViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        studyViewModel.dueCard.observe(viewLifecycleOwner) {
            studyViewModel.card = it
            binding.invalidateAll()
        }

        binding.cardAnswerBtn.setOnClickListener {
            studyViewModel.card?.answered = true
            binding.invalidateAll()
        }

        binding.cardEasyBtn.setOnClickListener {
            studyViewModel.update(5)
            binding.invalidateAll()
        }

        binding.cardHardBtn.setOnClickListener {
            studyViewModel.update(0)
            binding.invalidateAll()
        }

        binding.cardDoubtBtn.setOnClickListener {
            studyViewModel.update(3)
            binding.invalidateAll()
        }


        return binding.root

    }
}