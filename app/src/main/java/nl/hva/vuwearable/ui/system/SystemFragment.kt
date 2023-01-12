package nl.hva.vuwearable.ui.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import nl.hva.vuwearable.databinding.FragmentSystemBinding
import nl.hva.vuwearable.ui.login.LoginViewModel

class SystemFragment : Fragment() {

    private var _binding: FragmentSystemBinding? = null

    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSystemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!loginViewModel.checkIfUserIsLoggedIn()) {
            binding.measurementGroup.visibility = View.INVISIBLE
        }

        return root
    }

}