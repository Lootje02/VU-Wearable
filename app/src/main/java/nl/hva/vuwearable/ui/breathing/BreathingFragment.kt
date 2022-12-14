package nl.hva.vuwearable.ui.breathing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.FragmentBreathingSetupBinding
import kotlin.math.max

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
        val breatheIn = binding.seekbarBreatheIn.progress
        val breatheOut = binding.seekbarBreatheOut.progress
        val maxDuration = binding.seekbarDuration.progress

        breathingViewModel.breatheIn.value = breatheIn.toChar()
        breathingViewModel.breatheOut.value = breatheOut.toChar()
        breathingViewModel.maxDuration.value = maxDuration.toChar()
    }
}