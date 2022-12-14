package nl.hva.vuwearable.ui.breathing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BreathingViewModel : ViewModel() {
    val breatheIn = MutableLiveData('0')
    val breatheOut = MutableLiveData('0')
    val maxDuration = MutableLiveData('0')

    fun setBreatheIn() {
        breatheIn.value = breatheIn.value
    }

    fun setBreatheOut() {
        breatheOut.value = breatheOut.value
    }

    fun setmaxDuration() {
        maxDuration.value = maxDuration.value
    }
}