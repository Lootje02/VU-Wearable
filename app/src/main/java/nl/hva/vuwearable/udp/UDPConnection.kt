package nl.hva.vuwearable.udp

import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException

class UDPConnection : Runnable {

    override fun run() {

        try {
            val udpSocket = DatagramSocket(1234)
            val buffer = ByteArray(2048)
            val packet = DatagramPacket(buffer, 2048)
            while (true) {
                Log.i("UDP", "waiting to receive")
                udpSocket.receive(packet)
                val text = String(packet.data, 0, packet.data.size)
                Log.i("UDP", text)
            }
        } catch (e: SocketException) {
            Log.e("UDP", "Socket error", e)
        } catch (e: IOException) {
            Log.e("UDP", "IO error", e)
        }
    }

}