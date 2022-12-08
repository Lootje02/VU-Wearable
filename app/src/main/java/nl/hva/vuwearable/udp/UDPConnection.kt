package nl.hva.vuwearable.udp

import android.util.Log
import nl.hva.vuwearable.decoding.ASection
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Handles the connection with UDP and decodes the data that is coming in.
 *
 * @author Bunyamin Duduk
 *
 * @property setConnectedCallback gives back the connection in a callback
 * @property setASectionMeasurement gives back the A section measurement in a callback
 */
class UDPConnection(
    private val setConnectedCallback: (isConnected: Boolean) -> Unit,
    private val setASectionMeasurement: (measurements: Map<Int, Array<Number>>) -> Unit
) : Runnable {

    companion object {
        const val UDP_TAG = "UDP"
        const val UDP_PORT = 1234
        const val BUFFER_LENGTH = 2048
        const val CONNECTION_TIMEOUT_SECONDS = 3
    }

    override fun run() {
        // With the lastReceivedPacketDate, we can check if the packets are coming in at time
        var lastReceivedPacketDate: Date? = null

        // Check on a different thread if packets are coming in
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            // If no packets are received yet
            if (lastReceivedPacketDate === null) {
                Log.i(UDP_TAG, "No stable connection")
                setConnectedCallback(false)
                return@scheduleAtFixedRate
            }

            val currentDate = Date()
            val diff = currentDate.time - lastReceivedPacketDate!!.time
            val secondsDifference = diff / 1000

            // Check if the time difference is bigger or equal than the timeout
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
            // Connect to the UDP socket by the UDP port
            val udpSocket = DatagramSocket(UDP_PORT)
            val buffer = ByteArray(BUFFER_LENGTH)
            val packet = DatagramPacket(buffer, buffer.size)
            var i = 0

            val aDecoding = ASection()

            val byteBuffer = ByteBuffer.allocateDirect(100000000)
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)

            while (true) {
                Log.i(UDP_TAG, "Waiting to receive")
                udpSocket.receive(packet)

                setASectionMeasurement(aDecoding.convertBytes(packet.data, byteBuffer))

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