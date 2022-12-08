package nl.hva.vuwearable.ui.chart.scichart

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.scichart.charting.model.dataSeries.XyDataSeries
import com.scichart.charting.modifiers.*
import com.scichart.charting.visuals.axes.IAxis
import com.scichart.charting.visuals.axes.NumericAxis
import com.scichart.charting.visuals.renderableSeries.FastLineRenderableSeries
import com.scichart.charting.visuals.renderableSeries.IRenderableSeries
import com.scichart.core.annotations.Orientation
import com.scichart.core.framework.UpdateSuspender
import com.scichart.core.model.DoubleValues
import com.scichart.core.model.IntegerValues
import com.scichart.data.model.DoubleRange
import com.scichart.drawing.common.SolidPenStyle
import com.scichart.drawing.utility.ColorUtil
import nl.hva.vuwearable.databinding.FragmentSciChartBinding
import nl.hva.vuwearable.decoding.ASection
import java.util.*

/**
 * Initializes and updates the chart in real time.
 *
 * @author Bunyamin Duduk
 */
class SciChartFragment : Fragment() {

    private var _binding: FragmentSciChartBinding? = null

    private val chartViewModel: ChartViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val ecgLineData = DoubleValues()
    private val ecgLineDataSeries =
        XyDataSeries(Int::class.javaObjectType, Double::class.javaObjectType).apply {
            append(xValues, yValues)
        }

    private val icgLineData = IntegerValues()
    private val icgLineDataSeries =
        XyDataSeries(Int::class.javaObjectType, Int::class.javaObjectType).apply {
            append(xValues, yValues)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSciChartBinding.inflate(inflater, container, false)

        val surface = binding.surface

        // Create x and y axis
        val xAxis: IAxis = NumericAxis(requireContext())
        val yAxis: IAxis = NumericAxis(requireContext())


//        xAxis.visibleRangeLimit = DoubleRange(0.0, 10000.0)

        // Name of the line
        ecgLineDataSeries.seriesName = "ECG"
        icgLineDataSeries.seriesName = "ICG"

        // How much it will show on the screen
        ecgLineDataSeries.fifoCapacity = 500
        icgLineDataSeries.fifoCapacity = 10000

        // Add some padding at the bottom and top to have a more clear view
        yAxis.growBy = DoubleRange(10.0, 10.0)

        val xValues = IntegerValues()

        // Append data to initialise the data series
        ecgLineDataSeries.append(xValues, ecgLineData)
        icgLineDataSeries.append(xValues, icgLineData)

        // Type of line
        val ecgLineSeries: IRenderableSeries = FastLineRenderableSeries()
        ecgLineSeries.dataSeries = ecgLineDataSeries

//        val pointMarker = EllipsePointMarker()
//        pointMarker.setSize(10, 10)
//        pointMarker.strokeStyle = SolidPenStyle(Color.GREEN,false, 2.0f, null)
//        pointMarker.fillStyle = SolidBrushStyle(Color.RED)
//        ecgLineSeries.pointMarker = pointMarker


        val icgLineSeries: IRenderableSeries = FastLineRenderableSeries()
        icgLineSeries.dataSeries = icgLineDataSeries


        // Color of the line
        ecgLineSeries.strokeStyle = SolidPenStyle(ColorUtil.LimeGreen, true, 5f, null)
        icgLineSeries.strokeStyle = SolidPenStyle(ColorUtil.Yellow, true, 5f, null)

        // Show in a box what the lines are
        val legendModifier = LegendModifier(requireContext())
        legendModifier.setOrientation(Orientation.HORIZONTAL)
        legendModifier.setLegendPosition(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0, 0, 10)

        // Add all those data and modifiers
        UpdateSuspender.using(surface) {
            Collections.addAll(surface.renderableSeries, ecgLineSeries, icgLineSeries)
            Collections.addAll(
                surface.chartModifiers,
                PinchZoomModifier(),
                ZoomPanModifier(),
                ZoomExtentsModifier(),
            )
            Collections.addAll(surface.chartModifiers, legendModifier)
            Collections.addAll(surface.chartModifiers, RolloverModifier())
        }

        // Add the x and y axis to the chart
        UpdateSuspender.using(surface) {
            Collections.addAll(surface.xAxes, xAxis)
            Collections.addAll(surface.yAxes, yAxis)
        }

        ecgLineDataSeries.acceptsUnsortedData = true
        // Append the first value
//        ecgLineDataSeries.append(0, 0)
//        icgLineDataSeries.append(0, 0)

        var prevTime = 0
        // Observe all the incoming measurements from the UDP socket
        chartViewModel.sectionAMeasurements.observe(viewLifecycleOwner) {
            // Loop through the properties in an 'A' section
            for (mutableEntry in it) {
                val key = mutableEntry.key

                // Find the section that is ECG
//                val ecgValue = mutableEntry.value.find { measurement ->
//                    measurement.title == "ECG"
//                }
//
//                val time = mutableEntry.value.find { measurement ->
//                    measurement.title == "Tickcount"
//                }
                val tickCount = mutableEntry.value[ASection.TICK_COUNT_INDEX]
                val ecgValue = mutableEntry.value[ASection.ECG_INDEX]
//                Log.i("tickCount", ecgValue.toString())


//                if (it. != null && tickCount != null) {

                prevTime = tickCount as Int
                ecgLineDataSeries.append(tickCount, ecgValue as Double)
//                }

                // Find the section that is ICG
//                val icgValue = mutableEntry.value.find { measurement ->
//                    measurement.title == "ICG"
//                }

//                if (icgValue != null) {
//                    // If the key is not in the chart, then append it.
//                    // This is done to prevent duplicates
//                    if (icgLineDataSeries.xValues.size > 0 && icgLineDataSeries.xValues.last() < key && !icgLineDataSeries.xValues.contains(
//                            key
//                        )
//                    )
//                        icgLineDataSeries.append(key, icgValue.value)
//                }

                // Update where the person looks at the chart, so that they don't have
                // to manually scroll
                binding.surface.zoomExtentsX()
//                binding.surface.zoomExtentsY()

            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}