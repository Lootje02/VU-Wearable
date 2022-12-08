package nl.hva.vuwearable.ui.dashboard

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    val steps = MutableLiveData(0)

    val batteryPercentage = MutableLiveData<Int>().apply {
        value = 100
    }

    fun incrementSteps(){
        steps.value = steps.value!! + 1
    }


}