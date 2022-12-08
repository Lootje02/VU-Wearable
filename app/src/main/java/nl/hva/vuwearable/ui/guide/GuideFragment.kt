package nl.hva.vuwearable.ui.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
            findNavController().navigate(R.id.action_guideFragment_to_navigation_dashboard)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}