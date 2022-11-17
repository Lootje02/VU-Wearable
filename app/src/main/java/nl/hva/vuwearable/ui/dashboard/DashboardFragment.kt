package nl.hva.vuwearable.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import nl.hva.vuwearable.databinding.FragmentDashboardBinding


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

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setStepCount()

        return root
    }

    private fun setStepCount() {
        var count = 0
        val handler = Handler(Looper.getMainLooper())
        binding.tvStepsValue.text = "0"

        handler.postDelayed(object : Runnable {
            override fun run() {
                count++
                binding.tvStepsValue.text = count.toString()
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    private fun setAirPressure() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed(object : Runnable {
            override fun run() {
                var randomInt = (1000..1030).random()
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    private fun setEcg() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed(object : Runnable {
            override fun run() {
                var randomInt = (90..140).random()
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    private fun setIcg() {
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed(object : Runnable {
            override fun run() {
                var randomInt = (1000..1030).random()
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }
}