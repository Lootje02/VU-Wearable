package nl.hva.vuwearable.decoding

import java.nio.ByteBuffer
import java.util.*

/**
 * Parses and decodes the A section of a packet
 *
 * @author Bunyamin Duduk
 */
class ASection : PacketDecoding {

    companion object {
        const val A_FIRST_BYTE: Byte = 65
        const val A_SECOND_BYTE: Byte = 28
        const val A_THIRD_BYTE: Byte = 0

        const val A_PART_LENGTH = 28

        // When accessing the array, use those indexes to get the specific value
        const val TICK_COUNT_INDEX = 0
        const val ICG_INDEX = 1
        const val ECG_INDEX = 2

        val ECG_FORMULA = { value: Int -> PacketDecoding.A0_ALL + PacketDecoding.A1_ALL * value }
        val ICG_FORMULA = { value: Int -> PacketDecoding.A0_ALL + PacketDecoding.A1_ALL * value }
    }

    override fun parsePacket(data: ByteArray): LinkedHashMap<Int, ByteArray> {
        val array = LinkedList<Byte>()
        var isInASection = false
        var i = 0

        // Loop through each of the characters in the encoded packet
        data.forEachIndexed { index, byte ->
            // To check if we are at the 'A' section, check if the first bytes are corresponding to a real 'A' section
            if (!isInASection && byte == A_FIRST_BYTE && data[index + 1] == A_SECOND_BYTE && data[index + 2] == A_THIRD_BYTE) {
                isInASection = true
            }

            // If A is fully parsed
            if (i == A_PART_LENGTH) {
                i = 0
                isInASection = false
            }

            // Add the char code if we are in the 'A' section
            if (isInASection) {
                i++
                array.add(byte)
            }
        }

        return separateIntoSections(array)
    }

    override fun separateIntoSections(array: LinkedList<Byte>): LinkedHashMap<Int, ByteArray> {
        val map = LinkedHashMap<Int, ByteArray>()

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
            map[map.size] = subList.toByteArray()
            currentStart += A_PART_LENGTH
        }

        return map
    }

    override fun convertBytes(array: ByteArray, byteBuffer: ByteBuffer): Map<Int, Array<Number>> {
        val parsedSections = parsePacket(array)
        val results = mutableMapOf<Int, Array<Number>>()

        parsedSections.values.forEachIndexed { index, sectionArray ->
            // Get tick count section
            val tickCountArray = byteArrayOf(
                sectionArray[4],
                sectionArray[5],
                sectionArray[6],
                sectionArray[7]
            )

            // Get ICG section
            val icgArray = byteArrayOf(
                sectionArray[12],
                sectionArray[13],
                sectionArray[14],
                sectionArray[15]
            )

            // Get ECG section
            val ecgArray = byteArrayOf(
                sectionArray[16],
                sectionArray[17],
                sectionArray[18],
                sectionArray[19]
            )

            // Put them in the right order in the array so it can be accessed via the constants indexes
            results[index] = arrayOf(
                byteBuffer.getInt(tickCountArray),
                ICG_FORMULA(byteBuffer.getInt(icgArray)),
                ECG_FORMULA(byteBuffer.getInt(ecgArray))
            )

        }
        return results
    }
}