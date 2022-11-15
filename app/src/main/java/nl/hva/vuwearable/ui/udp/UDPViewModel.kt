package nl.hva.vuwearable.ui.udp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UDPViewModel : ViewModel() {

    val isConnected = MutableLiveData(false)

    fun setIsConnected(isConnected: Boolean) {
        this.isConnected.value = isConnected
    }

}