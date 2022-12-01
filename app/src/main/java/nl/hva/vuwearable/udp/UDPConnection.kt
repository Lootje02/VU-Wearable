package nl.hva.vuwearable.udp

import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class UDPConnection(private val setConnectedCallback: (isConnected: Boolean) -> Unit, private val firstDelay: Long = 3, private val everyDelay: Long = 3) : Runnable {

    companion object {
        const val UDP_TAG = "TAG"
        const val CONNECTION_TIMEOUT_SECONDS = 3
        const val UDP_PORT = 1234
        const val BUFFER_LENGTH = 2048
    }


    override fun run() {
        var lastReceivedPacketDate: Date? = null

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            if (lastReceivedPacketDate === null) {
                //Log.i(UDP_TAG, "No stable connection")
                setConnectedCallback(false)
                return@scheduleAtFixedRate
            }

            val currentDate = Date()
            val diff = currentDate.time - lastReceivedPacketDate!!.time
            val secondsDifference = diff / 1000

            // Connection is not stable
            if (secondsDifference >= CONNECTION_TIMEOUT_SECONDS) {
                //Log.i(UDP_TAG, "No stable connection!")
                setConnectedCallback(false)
            } else {
                // Connection is stable
               // Log.i(UDP_TAG, "Stable connection")
                setConnectedCallback(true)
            }
        }, firstDelay , everyDelay , TimeUnit.SECONDS)
        try {
            val udpSocket = DatagramSocket(UDP_PORT)
            val buffer = ByteArray(BUFFER_LENGTH)
            val packet = DatagramPacket(buffer, buffer.size)

            while (true) {
                //Log.i(UDP_TAG, "Waiting to receive")
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

}