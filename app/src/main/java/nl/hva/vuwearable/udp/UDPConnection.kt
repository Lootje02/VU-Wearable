package nl.hva.vuwearable.udp

import android.util.Log
import nl.hva.vuwearable.models.Measurement
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
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
    private val setMeasurementCallback: (measurements: LinkedHashMap<Long, List<Measurement>>) -> Unit
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
        private val ICG = Measurement(32, "ICG") { value: Long -> A0_ALL + A1_ALL * value }
        private val ECG = Measurement(32, "ECG") { value: Long -> A0_ALL + A1_ALL * value }
        private val IRSC = Measurement(32, "IRSC") { value: Long -> A0_ALL + A1_ALL * value }
        private val T = Measurement(32, "T") { value: Long -> A0_T + A1_T * value }


        private val TYPE_A_DATA_SET = listOf(HEADER, TICK_COUNT, STATUS, ICG, ECG, IRSC, T)

        private const val A_PART_LENGTH = 28
    }

    private var currentTime = 0L

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
    private fun getPartOfA(text: String): Map<Int, ByteArray> {
        val charArray = text.toByteArray(charset = Charset.forName("ISO-8859-1"))
        val array = mutableListOf<Byte>()

        var isInASection = false
        var i = 0

        // Loop through each of the characters in the encoded packet
        charArray.forEach {
            val code = it

            // When we arrive at the 'A' section
            if (it == 65.toByte()) {
                isInASection = true
            }

            if (i == 27) {
                i = 0
                isInASection = false
            }

            // Add the char code if we are in the 'A' section
            if (isInASection) {
                i++
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
    private fun splitIntoSections(array: List<Byte>): Map<Int, ByteArray> {
        val map = mutableMapOf<Int, ByteArray>()

        var currentStart = 0

        /*
         Till the end of the array, split up all the 'A's into its own section.
         Example:
         (65 char code == 'A')
         List: [65, 49 ,45, 65, 34, 98]
         Into: 0: [65, 49, 45], 1: [65, 34, 98]
         */
        while (currentStart + A_PART_LENGTH <= array.size - 1) {
            val subList = array.subList(currentStart, currentStart + A_PART_LENGTH)
            val byteArray = ByteArray(subList.size)
            subList.forEachIndexed { index, i ->
                byteArray[index] = i
            }
            map[map.size] = byteArray
            currentStart += A_PART_LENGTH
        }

        return map
    }

    private fun getMeasurementValuesForTypeA(map: Map<Int, ByteArray>): LinkedHashMap<Long, List<Measurement>> {
        val results = LinkedHashMap<Long, List<Measurement>>()
        val byteToBit = 8

        map.values.forEach { measurement ->
            val measurements = mutableListOf<Measurement>()
            var startCount = 0
            var timeInUnix = 0L
            TYPE_A_DATA_SET.forEach { type ->
                val totalElementToCount = type.totalBytes / byteToBit

                var calculatedUInt = 0L

                val unsignedArray: IntArray = IntArray(28)
//
                Log.i("size", measurement.size.toString())
                if (measurement.size == 28) {
                    measurement.forEachIndexed { index, byte ->
                        unsignedArray[index] = byte and 0xFF
                    }
                    Log.i("array", unsignedArray.contentToString())
                }


//                calculatedUInt = ByteBuffer.wrap(measurement).long


                if (type.title == "ECG") {
//                    val firstI = ((unsignedArray[15]and 0xFF) shl 24)
//                    val secondI = ((unsignedArray[14]and 0xFF) shl 16)
//                    val thirdI = ((unsignedArray[13]and 0xFF) shl 8)
//                    val fourtI = (unsignedArray[12] and 0xFF)
                    val byteBuffer = ByteBuffer.allocateDirect(1000000)
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
                    byteBuffer.put(measurement[12])
                    byteBuffer.put(measurement[13])
                    byteBuffer.put(measurement[14])
                    byteBuffer.put(measurement[15])
                    calculatedUInt = byteBuffer.long
//                    for (i in 12..15) {
//                        calculatedUInt = (calculatedUInt shl 8) + (unsignedArray[i] and 0xFF)
//                    }
                }

                if (type.title == TIME_TITLE) {
//                        val firstI = ((unsignedArray[7] and 0xFF))
//                        val secondI = ((unsignedArray[6] and 0xFF))
//                        val thirdI = ((unsignedArray[5] and 0xFF))
//                        val fourtI = (unsignedArray[4] and 0xFF)
//                        calculatedUInt = (firstI or secondI or thirdI or fourtI).toLong()
                    val byteBuffer = ByteBuffer.allocateDirect(1000000)
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
                    byteBuffer.put(measurement[4])
                    byteBuffer.put(measurement[5])
                    byteBuffer.put(measurement[6])
                    byteBuffer.put(measurement[7])
                    Log.i("buffer", byteBuffer.get(0).toString())
                    calculatedUInt = byteBuffer.long
                }
//                calculatedUInt = getUnsignedInt(measurement)
//                for (i in 0..3) {
//                    calculatedUInt += measurement[i] shl 8
//                }

//                calculatedUInt = measurement[3]
                // Convert the byte array to an UInt
//                Log.i("yes", measurement.toString())
//                for (i in 0..3) {
//                    calculatedUInt = (calculatedUInt shl 8) + (measurement[i].toUByte() and 0xFF.toUByte()).toInt()
//                }
//                for (i in 0..3) {
////                    calculatedUInt =
////                        (calculatedUInt * 10u) + ((measurement[i + startCount] and 0xff.toByte())).toUInt()
//                    calculatedUInt =
//                }

                type.value =
                    if (type.formula != null) type.formula?.let { it(calculatedUInt).toLong() }!! else calculatedUInt

                if (type.title == TIME_TITLE) {
                    currentTime += type.value
                    timeInUnix = currentTime
                }

                measurements.add(type)
                startCount += totalElementToCount
            }

            results[timeInUnix] = measurements
        }
        return results
    }


}

infix fun Byte.shl(that: Int): Int = this.toInt().shl(that)
infix fun Int.shl(that: Byte): Int =
    this.shl(that.toInt()) // Not necessary in this case because no there's (Int shl Byte)

infix fun Byte.shl(that: Byte): Int =
    this.toInt().shl(that.toInt()) // Not necessary in this case because no there's (Byte shl Byte)

//infix fun UByte.shl(that: Byte): Int = this.toInt().shl(that.toInt())
//infix fun UByte.shl(that: Int): Int = this.toInt().shl(that)
//
//infix fun UByte.and(that: Int): Int = this.toInt().and(that)
//infix fun UByte.and(that: UByte): Int = this.toInt().and(that.toInt())

infix fun Byte.and(that: Int): Int = this.toInt().and(that)
infix fun Int.and(that: Byte): Int =
    this.and(that.toInt()) // Not necessary in this case because no there's (Int and Byte)

infix fun Byte.and(that: Byte): Int =
    this.toInt().and(that.toInt()) // Not necessary in this case because no there's (Byte and Byte)