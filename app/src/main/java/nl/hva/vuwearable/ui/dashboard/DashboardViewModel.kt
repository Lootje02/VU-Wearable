package nl.hva.vuwearable.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val steps = MutableLiveData(0)

    fun incrementSteps(){
        steps.value = steps.value!! + 1
    }
}