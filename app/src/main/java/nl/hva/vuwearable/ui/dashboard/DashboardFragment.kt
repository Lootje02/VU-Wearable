package nl.hva.vuwearable.ui.dashboard

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import nl.hva.vuwearable.databinding.FragmentDashboardBinding
import java.util.*


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        setStepCount()

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    private fun setStepCount(){
        object : CountDownTimer(1000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvStepsNumber.text = (millisUntilFinished / 1000).toString()
            }
            override fun onFinish() {
                Log.i("finished","timer stoppped")
            }
        }.start()
    }
}