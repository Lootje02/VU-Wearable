package nl.hva.vuwearable.ui.breathing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BreathingViewModel : ViewModel() {
    var breatheIn = MutableLiveData(0)
    var breatheOut = MutableLiveData(0)
    var maxDuration = MutableLiveData(0)
}