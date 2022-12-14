package nl.hva.vuwearable.ui.breathing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewAnimator
import androidx.core.view.ViewCompat.animate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import nl.hva.vuwearable.R
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

        Log.i("EXCERCISE",breathingViewModel.breatheIn.value.toString())
        Log.i("EXCERCISE",breathingViewModel.breatheOut.value.toString())
        Log.i("EXCERCISE",breathingViewModel.maxDuration.value.toString())

        return root
    }

    fun startAnimation(){
        
    }

}