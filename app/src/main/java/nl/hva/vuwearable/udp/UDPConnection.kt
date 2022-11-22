package nl.hva.vuwearable.udp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class UDPConnection(private val context: Context,
                    private val setConnectedCallback: (isConnected: Boolean) -> Unit) : Runnable {

    companion object {
        const val UDP_TAG = "TAG"
        const val CONNECTION_TIMEOUT_SECONDS = 3
        const val UDP_PORT = 1234
        const val BUFFER_LENGTH = 2048
        const val DEVICE_NETWORK_NAME = "AndroidWifi"
    }

    override fun run() {
        var lastReceivedPacketDate: Date? = null

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            if (lastReceivedPacketDate === null || !userIsOnline()) {
                Log.i(UDP_TAG, "No stable connection")
                setConnectedCallback(false)
                return@scheduleAtFixedRate
            }

            val currentDate = Date()
            val diff = currentDate.time - lastReceivedPacketDate!!.time
            val secondsDifference = diff / 1000

            // Connection is not stable
            if (secondsDifference >= CONNECTION_TIMEOUT_SECONDS) {
                Log.i(UDP_TAG, "No stable connection!")
                setConnectedCallback(false)
            } else {
                // Connection is stable
                Log.i(UDP_TAG, "Stable connection")
                setConnectedCallback(true)
            }
        }, 3, 3, TimeUnit.SECONDS)
        try {
            val udpSocket = DatagramSocket(UDP_PORT)
            val buffer = ByteArray(BUFFER_LENGTH)
            val packet = DatagramPacket(buffer, buffer.size)

            while (true) {
                Log.i(UDP_TAG, "Waiting to receive")
                udpSocket.receive(packet)

                // Receive and show the incoming packet data
                // val text = String(packet.data, 0, packet.data.size)
                // Log.i(UDP_TAG, text)

                // Set the last received date to see if there is a delay between next packet
                lastReceivedPacketDate = Date()
            }
        } catch (e: SocketException) {
            Log.e(UDP_TAG, "Socket error", e)
            setConnectedCallback(false)
        } catch (e: IOException) {
            Log.e(UDP_TAG, "IO error", e)
            setConnectedCallback(false)
        }
    }

    /**
     * Function which checks if the current user is connected to the correct network (if any)
     * and checks if the current network (if available) has WiFi capabilities
     */
    private fun userIsOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        var ssid: String? = null
        val wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo: WifiInfo = wifiManager.connectionInfo
        if (wifiInfo.supplicantState == SupplicantState.COMPLETED) {
            // remove double quotes from ssid format
            ssid = wifiInfo.ssid.replace("\"", "")
        }

        return ssid.toString() == DEVICE_NETWORK_NAME && capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }


}