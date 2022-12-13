package nl.hva.vuwearable.ui.chart.scichart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

/**
 * @author Bunyamin Duduk
 */
class ChartViewModel : ViewModel() {

    val sectionAMeasurements: MutableLiveData<TreeMap<Int, Array<Number>>> = MutableLiveData()

    fun setMeasurement(measurements: TreeMap<Int, Array<Number>>) {
        this.sectionAMeasurements.value = measurements
    }

}