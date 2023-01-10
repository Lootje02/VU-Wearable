package nl.hva.vuwearable.websocket

import android.util.Log
import nl.hva.vuwearable.BuildConfig
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URISyntaxException

class Socket {
    private lateinit var client: WebSocketClient
    private var isConnected = false

    fun openConnection() {
        val uri: URI = try {
            URI(BuildConfig.WEB_SOCKET_URL)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }

        client = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.i("Websocket", "Opened")
                isConnected = true
            }

            override fun onMessage(message: String?) {
                Log.i("Websocket", "Received")
            }

            override fun onClose(code: Int, reason: String, remote: Boolean) {
                Log.i("Websocket", "Closed $reason")
            }

            override fun onError(ex: Exception) {
                Log.i("Websocket", "Error " + ex.message)
            }
        }
        client.connect()
    }
}