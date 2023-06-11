package es.uam.eps.dadm.cardsjavier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import es.uam.eps.dadm.cardsjavier.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_startFragment_to_deckListFragment)
        }

        return  binding.root
    }
}