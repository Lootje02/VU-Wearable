package nl.hva.vuwearable.ui.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.FragmentSystemBinding
import nl.hva.vuwearable.ui.login.LoginViewModel
import nl.hva.vuwearable.websocket.SocketService

/**
 * Controls the device configuration
 *
 * @author Bunyamin Duduk
 */
class SystemFragment : Fragment() {

    private var _binding: FragmentSystemBinding? = null

    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by activityViewModels()
    private lateinit var webSocket: SocketService

    companion object {
        // These strings are recognized by the device to do the specific action
        const val LIVE_DATA_START = "3a"
        const val LIVE_DATA_STOP = "0a"
        const val MEASUREMENT_START = "r"
        const val MEASUREMENT_STOP = "s"
        const val SHUT_DOWN_DEVICE = "Q"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSystemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loginViewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            // Show the start stop measuring data group based on if the user is a professor or not
            binding.measurementGroup.visibility = if (isLoggedIn) View.VISIBLE else View.INVISIBLE
            binding.shutdownGroup.visibility = if (isLoggedIn) View.VISIBLE else View.INVISIBLE
        }


        webSocket = SocketService()
        webSocket.openConnection()

        binding.liveDataStartButton.setOnClickListener {
            webSocket.sendMessage(LIVE_DATA_START)
            Toast.makeText(context, getString(R.string.starting_receiving_data), Toast.LENGTH_SHORT)
                .show()
        }

        binding.liveDataStopButton.setOnClickListener {
            webSocket.sendMessage(LIVE_DATA_STOP)
            Toast.makeText(context, getString(R.string.stopped_receiving_data), Toast.LENGTH_SHORT)
                .show()
        }

        binding.measurementStartButton.setOnClickListener {
            webSocket.sendMessage(MEASUREMENT_START)
            Toast.makeText(context, getString(R.string.starting_measuring), Toast.LENGTH_SHORT)
                .show()
        }

        binding.measurementStopButton.setOnClickListener {
            webSocket.sendMessage(MEASUREMENT_STOP)
            Toast.makeText(context, getString(R.string.stopped_measuring), Toast.LENGTH_SHORT)
                .show()

        }

        binding.deviceOffButton.setOnClickListener {
            webSocket.sendMessage(SHUT_DOWN_DEVICE)
            Toast.makeText(context, getString(R.string.shutdown_device_confirm), Toast.LENGTH_SHORT)
                .show()
        }

        return root
    }

    // on destroy of view make the binding reference to null
    override fun onDestroy() {
        super.onDestroy()
        // Close websocket connection
        webSocket.closeConnection()
        _binding = null
    }
}