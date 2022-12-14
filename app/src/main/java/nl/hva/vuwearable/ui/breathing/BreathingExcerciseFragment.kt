package nl.hva.vuwearable.ui.breathing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import nl.hva.vuwearable.R
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

        Log.i("EXCERCISE", breathingViewModel.breatheIn.value.toString())

        //animateIn()

        animateOut()

        return root
    }

    private fun animateIn() {
        val animator = binding.viewAnimator

        animator.animate().alpha(1f).scaleY(1.4f).scaleX(1.4f).setStartDelay(500).setDuration(1000)
            .start()
    }

    private fun animateOut() {
        val animator = binding.viewAnimator

        when (breathingViewModel.breatheIn.value) {
            1 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_1sec))
            2 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_2sec))
            3 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_3sec))
            4 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_4sec))
            5 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_5sec))
            6 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_6sec))
            7 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_7sec))
            8 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_8sec))
            9 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_9sec))
            10 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_10sec))
        }
    }

}