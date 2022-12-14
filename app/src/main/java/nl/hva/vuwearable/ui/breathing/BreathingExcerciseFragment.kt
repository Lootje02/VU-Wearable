package nl.hva.vuwearable.ui.breathing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import nl.hva.vuwearable.databinding.FragmentBreathingExcerciseBinding

class BreathingExcerciseFragment: Fragment() {

    private var _binding: FragmentBreathingExcerciseBinding? = null

    private val binding get() = _binding!!

    private val breathingViewModel: BreathingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBreathingExcerciseBinding.inflate(inflater, container, false)

        val root: View = binding.root

        Log.i("hello",breathingViewModel.breatheOut.toString())
        Log.i("hello",breathingViewModel.breatheIn.toString())
        Log.i("hello",breathingViewModel.maxDuration.toString())

        return root
    }
}