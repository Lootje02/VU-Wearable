package nl.hva.vuwearable.decoding

import java.nio.ByteBuffer
import java.util.*

interface PacketDecoding {

    fun parsePacket(data: ByteArray): LinkedHashMap<Int, ByteArray>

    fun separateIntoSections(array: LinkedList<Byte>): LinkedHashMap<Int, ByteArray>

    fun convertBytes(array: ByteArray, byteBuffer: ByteBuffer): Map<Int, Array<Number>>

}

fun ByteBuffer.getInt(array: ByteArray): Int {
    this.clear()
    this.put(array)
    this.position(0)
    return this.int
}