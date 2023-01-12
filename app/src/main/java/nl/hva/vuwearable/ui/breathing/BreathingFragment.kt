package nl.hva.vuwearable.ui.breathing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
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

        binding.progressBreathInSeconds.text =
            getString(R.string.seconds_breathe_in, binding.seekbarBreatheIn.progress)

        binding.progressBreathOutSeconds.text =
            getString(R.string.seconds_breathe_out, binding.seekbarBreatheOut.progress)

        binding.progressMaxSeconds.text =
            getString(R.string.seconds_duration, binding.seekbarDuration.progress)

        binding.seekbarBreatheIn.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.progressBreathInSeconds.text = getString(R.string.seconds_breathe_in, progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.seekbarBreatheOut.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.progressBreathOutSeconds.text = getString(R.string.seconds_breathe_out, progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.seekbarDuration.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.progressMaxSeconds.text = getString(R.string.seconds_duration, progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        return root
    }

    fun getSeekBarData() {
        breathingViewModel.breatheIn.value = binding.seekbarBreatheIn.progress
        breathingViewModel.breatheOut.value = binding.seekbarBreatheOut.progress
        breathingViewModel.maxDuration.value = binding.seekbarDuration.progress
    }

}