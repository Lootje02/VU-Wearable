package nl.hva.vuwearable.ui.breathing

import nl.hva.vuwearable.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import nl.hva.vuwearable.databinding.FragmentBreathingExcerciseBinding


class BreathingExcerciseFragment : Fragment() {

    private var _binding: FragmentBreathingExcerciseBinding? = null

    private val binding get() = _binding!!

    private val breathingViewModel: BreathingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBreathingExcerciseBinding.inflate(inflater, container, false)

        val root: View = binding.root

        startAnimation()

        Log.i("EXCERCISE", breathingViewModel.breatheIn.value.toString())
        Log.i("EXCERCISE", breathingViewModel.breatheOut.value.toString())
        Log.i("EXCERCISE", breathingViewModel.maxDuration.value.toString())

        return root
    }

    private fun startAnimation() {
        val animator = binding.viewAnimator

        val inter =
            AnimationUtils.loadAnimation(context, R.anim.zoom_in) // load an animation

        animator.startAnimation(inter)
    }

}