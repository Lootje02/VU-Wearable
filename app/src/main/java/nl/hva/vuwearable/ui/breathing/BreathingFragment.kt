package nl.hva.vuwearable.ui.breathing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.FragmentBreathingSetupBinding

class BreathingFragment : Fragment() {

    private var _binding: FragmentBreathingSetupBinding? = null

    private val binding get() = _binding!!

    private val breathingViewModel: BreathingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBreathingSetupBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.btnStartExcercise.setOnClickListener {
            getSeekBarData()
            findNavController().navigate(R.id.action_breathingFragment_to_excerciseFragment)
        }

        return root
    }

    private fun getSeekBarData() {
        breathingViewModel.setBreatheIn()
        breathingViewModel.setBreatheOut()
        breathingViewModel.setmaxDuration()
    }
}