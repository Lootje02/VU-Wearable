package nl.hva.vuwearable.ui.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import nl.hva.vuwearable.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val viewModel: DashboardViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setStepCount()

        return root
    }

    private fun setStepCount() {
        viewModel.steps.observe(viewLifecycleOwner) {
            binding.tvStepsValue.text = it.toString()
        }

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed(object : Runnable {
            override fun run() {
                viewModel.incrementSteps()
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