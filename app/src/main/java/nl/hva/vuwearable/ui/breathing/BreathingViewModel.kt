package nl.hva.vuwearable.ui.breathing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textview.MaterialTextView

class BreathingViewModel : ViewModel() {
    val breatheIn = MutableLiveData(0)
    val breatheOut = MutableLiveData(0)
    val maxDuration = MutableLiveData(0)
}