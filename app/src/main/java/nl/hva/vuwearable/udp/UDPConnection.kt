package nl.hva.vuwearable.udp

import android.util.Log
import nl.hva.vuwearable.models.Measurement
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Handles the connection with UDP and decodes the data that is coming in.
 *
 * @author Bunyamin Duduk
 *
 * @property setConnectedCallback gives back the connection in a callback
 * @property setMeasurementCallback gives back the measurement in a callback
 */
class UDPConnection(
    private val setConnectedCallback: (isConnected: Boolean) -> Unit,
    private val setMeasurementCallback: (measurements: LinkedHashMap<Double, List<Measurement>>) -> Unit
) : Runnable {

    companion object {
        const val UDP_TAG = "UDP"
        const val UDP_PORT = 1234
        const val BUFFER_LENGTH = 2048
        const val CONNECTION_TIMEOUT_SECONDS = 3
        private const val TIME_TITLE = "Tickcount"

        private const val A0_ALL = 0.0
        private const val A1_ALL = 0.00047683721641078591
        private const val A0_T = 24.703470230102539
        private const val A1_T = 0.00097313715377822518

        private val HEADER = Measurement(32, "Header")
        private val TICK_COUNT = Measurement(32, "Tickcount")
        private val STATUS = Measurement(32, "Status")
        private val ICG = Measurement(32, "ICG") { value: Double -> A0_ALL + A1_ALL * value }
        private val ECG = Measurement(32, "ECG") { value: Double -> A0_ALL + A1_ALL * value }
        private val IRSC = Measurement(32, "IRSC") { value: Double -> A0_ALL + A1_ALL * value }
        private val T = Measurement(32, "T") { value: Double -> A0_T + A1_T * value }


        private val TYPE_A_DATA_SET = listOf(HEADER, TICK_COUNT, STATUS, ICG, ECG, IRSC, T)

        private const val A_PART_LENGTH = 28
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

            while (true) {
                Log.i(UDP_TAG, "Waiting to receive")
                udpSocket.receive(packet)

                // Receive and show the incoming packet data
                val text = String(packet.data, 0, packet.data.size)
                // Log.i(UDP_TAG, text)

                val map = getPartOfA(text)
                val results = getMeasurementValuesForTypeA(map)
                setMeasurementCallback(results)

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
     * Put all the character codes of the 'A' section into a map
     * since a packet can contain multiple 'A' sections.
     *
     * @param text The encoded packet
     * @return all the sections of 'A'
     */
    private fun getPartOfA(text: String): Map<Int, List<Int>> {
        val charArray = text.toCharArray()
        val array = mutableListOf<Int>()

        var isInASection = false

        // Loop through each of the characters in the encoded packet
        charArray.forEach {
            val code = it.code

            // After 'A' the character 'M' comes, then we know that we are
            // not in the 'A' section anymore
            if (it == 'M') {
                isInASection = false
            }

            // When we arrive at the 'A' section
            if (it == 'A') {
                isInASection = true
            }

            // Add the char code if we are in the 'A' section
            if (isInASection) {
                array.add(code)
            }
        }
        return splitIntoSections(array)
    }

    /**
     * Splits all the 'A' sections of one packet into a map.
     *
     * @param array all the 'A' sections of a packet
     * @return all the sections of 'A' sections
     */
    private fun splitIntoSections(array: List<Int>): Map<Int, List<Int>> {
        val map = mutableMapOf<Int, List<Int>>()

        var currentStart = 0

        /*
         Till the end of the array, split up all the 'A's into its own section.
         Example:
         (65 char code == 'A')
         List: [65, 49 ,45, 65, 34, 98]
         Into: 0: [65, 49, 45], 1: [65, 34, 98]
         */
        while (currentStart + A_PART_LENGTH <= array.size - 1) {
            map[map.size] = array.subList(currentStart, currentStart + A_PART_LENGTH)
            currentStart += A_PART_LENGTH
        }

        return map
    }

    private fun getMeasurementValuesForTypeA(map: Map<Int, List<Int>>): LinkedHashMap<Double, List<Measurement>> {
        val results = LinkedHashMap<Double, List<Measurement>>()
        val byteToBit = 8
        map.values.forEach { measurement ->
            val measurements = mutableListOf<Measurement>()
            var startCount = 0
            var timeInUnix = 0.0
            TYPE_A_DATA_SET.forEach { type ->
                val totalElementToCount = type.totalBytes / byteToBit

                var measurementTotal = 0.0
                for (i in startCount until totalElementToCount + startCount) {
                    measurementTotal += measurement[i]
                }
                type.value =
                    if (type.formula != null) type.formula?.let { it(measurementTotal) }!! else measurementTotal

                if (type.title == TIME_TITLE) timeInUnix = measurementTotal

                measurements.add(type)
                startCount += totalElementToCount
            }

            results[timeInUnix] = measurements
        }
        return results
    }


}