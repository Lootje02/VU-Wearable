package nl.hva.vuwearable.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.FragmentDashboardBinding
import nl.hva.vuwearable.ui.udp.UDPViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val udpViewModel: UDPViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navController = findNavController()

        binding.cvFaq.setOnClickListener {
            navController.navigate(R.id.action_navigation_dashboard_to_faqFragment)
        }

        binding.cvBreathing.setOnClickListener {
            navController.navigate(R.id.action_navigation_dashboard_to_breathingFragment)
        }

        binding.cvChart.setOnClickListener {
            navController.navigate(R.id.action_navigation_dashboard_to_navigation_chart)
        }

        binding.cvSetup.setOnClickListener {
            navController.navigate(R.id.action_navigation_dashboard_to_guideFragment)
        }

        binding.cvSystem.setOnClickListener {
            navController.navigate(R.id.action_navigation_dashboard_to_systemFragment)
        }

        connectionEstablished()

        return root
    }

    /**
     * Observe the udpviewmodel for changes in wifi connection and display appropriate icons
     */
    private fun connectionEstablished() {
        udpViewModel.isConnected.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.wifiIconImageview.setImageResource(R.drawable.wifi_icon)
                    binding.wifiSubTextview.text = getString(R.string.connection_success)

                    udpViewModel.isReceivingData.observe(viewLifecycleOwner) { isReceivingData ->
                        if (!isReceivingData) {
                            binding.wifiSubTextview.text = getString(R.string.no_data_connection)
                            binding.wifiIconImageview.setImageResource(R.drawable.wifi_not_receiving_icon)
                        }
                    }
                }
                false -> {
                    binding.wifiIconImageview.setImageResource(R.drawable.wifi_off_icon)
                    binding.wifiSubTextview.text = getString(R.string.connection_failed)
                }
            }
        }
    }
}