package nl.hva.vuwearable.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.FragmentDashboardBinding
import nl.hva.vuwearable.ui.udp.UDPViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    private val udpViewModel: UDPViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        binding.ivFaq.setOnClickListener {
//            findNavController().navigate(R.id.action_navigation_dashboard_to_faqFragment)
//        }
//
//        binding.ivBreathingWidget.setOnClickListener{
//            findNavController().navigate(R.id.action_navigation_dashboard_to_breathingFragment)
//        }

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