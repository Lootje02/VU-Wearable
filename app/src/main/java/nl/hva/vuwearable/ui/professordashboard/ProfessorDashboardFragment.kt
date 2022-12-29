/**
 * @author Hugo Zuidema
 */
package nl.hva.vuwearable.ui.professordashboard

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.FragmentProfesorDashboardBinding
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class ProfessorDashboardFragment : Fragment() {

    private var _binding: FragmentProfesorDashboardBinding? = null
    private val binding get() = _binding!!

    private var isRecording: Boolean = false
    private var testerId: String = "Not Set"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfesorDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        val time = Calendar.getInstance().time
        val formatter =  SimpleDateFormat.getDateTimeInstance().format(time)

        binding.txtSystemTime.text = getString(R.string.pd_current_system_time, formatter.format(time))
        binding.txtTestId.text = getString(R.string.pd_test_id, testerId)

        binding.btnFaq.setOnClickListener {
            findNavController().navigate(R.id.faqFragment)
        }

        binding.btnTesterId.setOnClickListener { showSetTesterIdDialog() }

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

    private fun updateTesterId(id: String) {
        if (id.isNotBlank()) {
            testerId = id
            binding.txtTestId.text = getString(R.string.pd_test_id, id)
        } else {
            Toast.makeText(activity, getString(R.string.tester_id_err), Toast.LENGTH_LONG).show()
        }
    }

    private fun showSetTesterIdDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.pd_set_tester_title))

        val dialogLayout = layoutInflater.inflate(R.layout.set_tester_dialog, null)
        val idInput = dialogLayout.findViewById<EditText>(R.id.tvTesterId)

        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.dialog_ok_button) { _: DialogInterface, _: Int ->
            updateTesterId(idInput.text.toString())
        }

        builder.show()
    }
}