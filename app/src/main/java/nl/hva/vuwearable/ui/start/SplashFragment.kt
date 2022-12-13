package nl.hva.vuwearable.ui.start

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.hva.vuwearable.MainActivity
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnDashboard.setOnClickListener {
            val intent = Intent(it.context, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnSetup.setOnClickListener {

        }

        return root
    }

}