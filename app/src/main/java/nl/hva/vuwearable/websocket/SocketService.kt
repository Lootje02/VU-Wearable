package nl.hva.vuwearable.websocket

import android.util.Log
import nl.hva.vuwearable.BuildConfig
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URISyntaxException

class SocketService {

    private lateinit var client: WebSocketClient
    private val WS_LOG_TAG = "WS-SERVICE"
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
                isConnected = true
                Log.i(WS_LOG_TAG, "Opened")
            }

            override fun onMessage(message: String?) {
                Log.i(WS_LOG_TAG, "Received")
            }

            override fun onClose(code: Int, reason: String, remote: Boolean) {
                isConnected = false
                Log.i(WS_LOG_TAG, "Closed $reason")
            }

            override fun onError(ex: Exception) {
                isConnected = false
                Log.i(WS_LOG_TAG, "Error " + ex.message)
            }
        }

        client.connect()
    }

    /**
     * Sends a message through the socket. This function will only send the message
     * if a connection has been established with the socket.
     * @param message the message to send
     * @return a boolean indicating whether the request was successful or not
     */
    fun sendMessage(message: String?): Boolean {
        return if (isConnected) {
            client.send(message)
            true
        } else {
            false
        }
    }

    /**
     * Closes the connection with the websocket
     */
    fun closeConnection() {
        client.close()
    }
}