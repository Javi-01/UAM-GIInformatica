package es.uam.eps.dadm.cardsjavier

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import es.uam.eps.dadm.cardsjavier.databinding.FragmentTitleBinding


class TitleFragment : Fragment() {

    private lateinit var binding: FragmentTitleBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTitleBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            binding.root.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_titleFragment_to_deckListFragment)
            }
        }else{
            binding.root.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_titleFragment_to_startFragment)
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

}