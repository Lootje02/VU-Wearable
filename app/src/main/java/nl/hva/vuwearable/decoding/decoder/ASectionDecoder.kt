package nl.hva.vuwearable.decoding.decoder

import nl.hva.vuwearable.decoding.PacketDecoding
import nl.hva.vuwearable.decoding.getInt
import nl.hva.vuwearable.decoding.models.ASection
import java.nio.ByteBuffer

/**
 * Parses and decodes the A section of a packet
 *
 * @author Bunyamin Duduk
 */
class ASectionDecoder : PacketDecoding<Map<Int, ASection>> {

    companion object {
        const val A_FIRST_BYTE: UByte = 65u
        const val A_SECOND_BYTE: UByte = 32u
        const val A_THIRD_BYTE: UByte = 0u

        const val A_PART_LENGTH = 32

        const val A0 = 0.0
        const val A1_ECG = 7.9472869401797652e-05
        const val A1_ICG = 0.00047683721641078591
        const val A0_T = 24.703470230102539
        const val A1_T = 0.00097313715377822518

        val ECG_FORMULA = { value: Int -> A0 + A1_ECG * value }
        val ICG_FORMULA = { value: Int -> A0 + A1_ICG * value }
    }

    override fun parsePacket(data: List<UByte>): LinkedHashMap<Int, List<UByte>> {
        val array = ArrayList<UByte>()
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

    override fun separateIntoSections(array: List<UByte>): LinkedHashMap<Int, List<UByte>> {
        val map = LinkedHashMap<Int, List<UByte>>()

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

    override fun convertBytes(array: List<UByte>, byteBuffer: ByteBuffer): Map<Int, ASection> {
        val parsedSections = parsePacket(array)
        val results = mutableMapOf<Int, ASection>()

        parsedSections.values.forEachIndexed { index, sectionArray ->
            /*
                The A-Section is as following:
                Header: 0-1-2-3
                Timestamp: 4-5-6-7
                Status: 8-9-10-11
                Vicg: 12-13-14-15
                V2ecg: 16-17-18-19
                Visrc: 20-21-22-23
                Vecg: 24-25-26-27
                T: 28-29-30-31
             */
            // Get tick count section
            val tickCountArray = arrayOf(
                sectionArray[4],
                sectionArray[5],
                sectionArray[6],
                sectionArray[7]
            )

            // Get ICG section
            val icgArray = arrayOf(
                sectionArray[12],
                sectionArray[13],
                sectionArray[14],
                sectionArray[15]
            )

            // Get ECG section
            val ecgArray = arrayOf(
                sectionArray[24],
                sectionArray[25],
                sectionArray[26],
                sectionArray[27]
            )

            // Put the result in the map with the corresponding values
            results[index] = ASection(
                byteBuffer.getInt(tickCountArray),
                ICG_FORMULA(byteBuffer.getInt(icgArray)),
                ECG_FORMULA(byteBuffer.getInt(ecgArray))
            )
        }
        return results
    }
}