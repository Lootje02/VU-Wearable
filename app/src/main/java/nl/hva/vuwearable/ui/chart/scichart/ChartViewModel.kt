package nl.hva.vuwearable.ui.chart.scichart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.hva.vuwearable.models.Measurement
import java.util.*

class ChartViewModel : ViewModel() {

    val measurements: MutableLiveData<TreeMap<Long, List<Measurement>>> = MutableLiveData()

    fun setMeasurement(measurements: TreeMap<Long, List<Measurement>>) {
        this.measurements.value = measurements
    }

}