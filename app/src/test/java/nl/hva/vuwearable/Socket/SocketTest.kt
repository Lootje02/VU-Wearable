package nl.hva.vuwearable.Socket

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import nl.hva.vuwearable.decoding.models.ASection
import nl.hva.vuwearable.udp.UDPConnection
import nl.hva.vuwearable.websocket.SocketService
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.util.*

/**
 * WireShark Test which tests the functionality of the WebSocket.
 * Please use wireshark also to confirm expected results.
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
class SocketTest {

    companion object {
        private val socketService = SocketService()
        private var isReceivingData = false
        private var isConnected = false
        private var measurements: TreeMap<Int, ASection> = TreeMap()

        private lateinit var thread: Thread
    }

    @Before
    fun setup() {
        thread = Thread {
            UDPConnection(
                InstrumentationRegistry.getInstrumentation().targetContext,
                3,
                3,
                setConnectedCallback = { isConnectedDevice, isReceivingDataDevice ->
                    isReceivingData = isReceivingDataDevice
                    isConnected = isConnectedDevice
                },
                setASectionMeasurement = { data ->
                    measurements = TreeMap(data)
                }
            )
        }

        socketService.openConnection()
    }

    @Test
    fun testAUseAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("nl.hva.vuwearable", appContext.packageName)
    }

    @Test
    fun testBCheckSetupIsSuccessful() {
        thread.start()

        // ensure the thread is running
        Assert.assertTrue(thread.isAlive)

        // allow a connection to be established, let this thread sleep
        Thread.sleep(10000)

        // ensure we are connected
        Assert.assertTrue(isConnected)
    }

    @Test
    fun testCStartMeasurement() {
        socketService.sendMessage("r")

        // give the socket time to process the message
        Thread.sleep(3000)
    }

    @Test
    fun testDStartLiveData() {
        // start live data
        socketService.sendMessage("3a")

        // give the socket time to process
        Thread.sleep(3000)

        // ensure we are receiving data
        Assert.assertTrue(isReceivingData)
    }

    @Test
    fun testEStopLiveData() {
        socketService.sendMessage("0a")
        Thread.sleep(3000)
        Assert.assertFalse(isReceivingData)
    }

    @Test
    fun testFStopMeasurement() {
        socketService.sendMessage("s")
        Thread.sleep(3000)
    }

    @Test
    fun testGPowerOffDevice() {
        socketService.sendMessage("Q")

        // allow the connection to be lost
        Thread.sleep(10000)
        Assert.assertFalse(isReceivingData)
        Assert.assertFalse(isConnected)
    }
}