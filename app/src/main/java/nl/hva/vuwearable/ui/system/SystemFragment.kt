package nl.hva.vuwearable.ui.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var webSocket: SocketService

    companion object {
        // Those strings are recognized by the device to do the specific action
        const val LIVE_DATA_START = "3a"
        const val LIVE_DATA_STOP = "0a"
        const val MEASUREMENT_START = "r"
        const val MEASUREMENT_STOP = "s"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSystemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!loginViewModel.checkIfUserIsLoggedIn()) {
            binding.measurementGroup.visibility = View.INVISIBLE
        }

        webSocket = SocketService()
        webSocket.openConnection()

        binding.liveDataStartButton.setOnClickListener {
            webSocket.sendMessage(LIVE_DATA_START)
            Toast.makeText(context, "Started receiving data", Toast.LENGTH_SHORT).show()
        }

        binding.liveDataStopButton.setOnClickListener {
            webSocket.sendMessage(LIVE_DATA_STOP)
            Toast.makeText(context, "Stopped receiving data", Toast.LENGTH_SHORT).show()
        }

        binding.measurementStartButton.setOnClickListener {
            webSocket.sendMessage(MEASUREMENT_START)
            Toast.makeText(context, "Started measuring", Toast.LENGTH_SHORT).show()
        }

        binding.measurementStopButton.setOnClickListener {
            webSocket.sendMessage(MEASUREMENT_STOP)
            Toast.makeText(context, "Stopped measuring", Toast.LENGTH_SHORT).show()

        }

        return root
    }

    override fun onDestroyView() {
        webSocket.closeConnection()
    }

}