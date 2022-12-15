package nl.hva.vuwearable.ui.chart.scichart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nl.hva.vuwearable.decoding.models.ASection
import java.util.*

/**
 * @author Bunyamin Duduk
 */
class ChartViewModel : ViewModel() {

    val sectionAMeasurements: MutableLiveData<TreeMap<Int, ASection>> = MutableLiveData()

    fun setASectionMeasurement(measurements: TreeMap<Int, ASection>) {
        this.sectionAMeasurements.value = measurements
    }

}