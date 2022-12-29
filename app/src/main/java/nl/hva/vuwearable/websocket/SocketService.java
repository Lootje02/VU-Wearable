/**
 * @author Hugo Zuidema
 */
package nl.hva.vuwearable.websocket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import nl.hva.vuwearable.BuildConfig;

public class SocketService {

    private WebSocketClient client;
    private boolean isConnected = false;

    /**
     * Opens a connection with the websocket on the device
     */
    public void openConnection() {
        URI uri;
        try {
            uri = new URI(BuildConfig.WEB_SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        client = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("Websocket", "Opened");
                isConnected = true;
            }

            @Override
            public void onMessage(String message) {
                Log.i("Websocket", "Received");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("Websocket", "Closed " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.i("Websocket", "Error " + ex.getMessage());
            }
        };

        client.connect();
    }

    /**
     * Sends a message through the socket. This function will only send the message
     * if a connection has been established with the socket.
     * @param message the message to send
     * @return a boolean indicating whether the request was successful or not
     */
    public boolean sendMessage(String message) {
        if (isConnected) {
            client.send(message);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Closes the connection with the websocket
     */
    public void closeConnection() {
        client.close();
    }
}
