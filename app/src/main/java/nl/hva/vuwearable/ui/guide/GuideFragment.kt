package nl.hva.vuwearable.ui.guide

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.hva.vuwearable.MainActivity
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.FragmentElectrodesGuideBinding

class GuideFragment : Fragment() {

    private var _binding: FragmentElectrodesGuideBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentElectrodesGuideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.guideFinishButton.setOnClickListener {
            val intent = Intent(it.context, MainActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}