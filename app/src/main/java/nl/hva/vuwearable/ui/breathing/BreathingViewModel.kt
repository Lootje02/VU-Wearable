package nl.hva.vuwearable.ui.breathing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BreathingViewModel : ViewModel() {
    val breatheIn = MutableLiveData(0)
    val breatheOut = MutableLiveData(0)
    val pause = MutableLiveData(0)
    val duration = MutableLiveData(0)
}