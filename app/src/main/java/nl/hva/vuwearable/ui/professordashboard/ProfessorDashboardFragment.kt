package nl.hva.vuwearable.ui.professordashboard

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.FragmentProfesorDashboardBinding

class ProfessorDashboardFragment : Fragment() {

    private var _binding: FragmentProfesorDashboardBinding? = null
    private val binding get() = _binding!!

    private var isRecording: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfesorDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.cvStartBlock.setOnClickListener {
            isRecording = if (isRecording) {
                updateStopBtn()
                false
            } else {
                updateStartBtn()
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateStartBtn() {
        binding.cvStartBlock.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.bg_red, null))

        binding.iconStart.setImageResource(R.drawable.ic_baseline_pause_24)
        binding.iconStart.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.system_error, null))

        binding.txtStart.text = getString(R.string.pd_stop_btn)
        binding.txtStart.setTextColor(resources.getColor(R.color.system_error, null))
    }

    private fun updateStopBtn() {
        binding.cvStartBlock.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.bg_turquoise, null))

        binding.iconStart.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        binding.iconStart.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.turquoise, null))

        binding.txtStart.text = getString(R.string.pd_start_btn)
        binding.txtStart.setTextColor(resources.getColor(R.color.turquoise, null))
    }
}