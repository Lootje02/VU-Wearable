package nl.hva.vuwearable.ui.udp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author Bunyamin Duduk
 * @author Hugo Zuidema
 */
class UDPViewModel : ViewModel() {

    val isConnected = MutableLiveData(false)
    val isReceivingData = MutableLiveData(false)

    fun setIsConnected(isConnected: Boolean) {
        this.isConnected.value = isConnected
    }

    fun setIsReceivingData(isReceivingData: Boolean) {
        this.isReceivingData.value = isReceivingData
    }

}