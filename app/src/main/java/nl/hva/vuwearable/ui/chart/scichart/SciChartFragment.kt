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
import nl.hva.vuwearable.R
import nl.hva.vuwearable.databinding.FragmentSciChartBinding
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

    private val twoEcgLineData = DoubleValues()
    private val twoEcgLineDataSeries =
        XyDataSeries(Int::class.javaObjectType, Double::class.javaObjectType).apply {
            append(xValues, yValues)
        }

    private val isrcLineData = IntegerValues()
    private val isrcLineDataSeries =
        XyDataSeries(Int::class.javaObjectType, Int::class.javaObjectType).apply {
            append(xValues, yValues)
        }

    private val ecgLineData = DoubleValues()
    private val ecgLineDataSeries =
        XyDataSeries(Int::class.javaObjectType, Double::class.javaObjectType).apply {
            append(xValues, yValues)
        }

    private val icgLineData = DoubleValues()
    private val icgLineDataSeries =
        XyDataSeries(Int::class.javaObjectType, Double::class.javaObjectType).apply {
            append(xValues, yValues)
        }

    private val temperatureLineData = DoubleValues()
    private val temperateLineDataSeries =
        XyDataSeries(Int::class.javaObjectType, Double::class.javaObjectType).apply {
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

        // Name of the line
        twoEcgLineDataSeries.seriesName = "2ECG"
        isrcLineDataSeries.seriesName = "ISRC"
        ecgLineDataSeries.seriesName = getString(R.string.ECG)
        icgLineDataSeries.seriesName = getString(R.string.ICG)
        temperateLineDataSeries.seriesName = "T"

        // How much it will show on the screen
        twoEcgLineDataSeries.fifoCapacity = 2000
        isrcLineDataSeries.fifoCapacity = 2000
        ecgLineDataSeries.fifoCapacity = 2000
        icgLineDataSeries.fifoCapacity = 5000
        temperateLineDataSeries.fifoCapacity = 2000

        // Add some padding at the bottom and top to have a more clear view
        yAxis.growBy = DoubleRange(0.3, 0.3)

        val xValues = IntegerValues()

        // Append data to initialise the data series
        twoEcgLineDataSeries.append(xValues, twoEcgLineData)
        isrcLineDataSeries.append(xValues, isrcLineData)
        ecgLineDataSeries.append(xValues, ecgLineData)
        icgLineDataSeries.append(xValues, icgLineData)
        temperateLineDataSeries.append(xValues, temperatureLineData)

        // Type of line
        val twoEcgLineSeries: IRenderableSeries = FastLineRenderableSeries()
        twoEcgLineSeries.dataSeries = twoEcgLineDataSeries

        val isrcLineSeries: IRenderableSeries = FastLineRenderableSeries()
        isrcLineSeries.dataSeries = isrcLineDataSeries

        val ecgLineSeries: IRenderableSeries = FastLineRenderableSeries()
        ecgLineSeries.dataSeries = ecgLineDataSeries

        val icgLineSeries: IRenderableSeries = FastLineRenderableSeries()
        icgLineSeries.dataSeries = icgLineDataSeries

        val temperatureLineSeries: IRenderableSeries = FastLineRenderableSeries()
        temperatureLineSeries.dataSeries = temperateLineDataSeries

        // Color of the line
        twoEcgLineSeries.strokeStyle = SolidPenStyle(ColorUtil.Green, true, 5f, null)
        isrcLineSeries.strokeStyle = SolidPenStyle(ColorUtil.Blue, true, 5f, null)
        ecgLineSeries.strokeStyle = SolidPenStyle(ColorUtil.LimeGreen, true, 5f, null)
        icgLineSeries.strokeStyle = SolidPenStyle(ColorUtil.Yellow, true, 5f, null)
        temperatureLineSeries.strokeStyle = SolidPenStyle(ColorUtil.Red, true, 5f, null)

        // Show in a box what the lines are
        val legendModifier = LegendModifier(requireContext())
        legendModifier.setOrientation(Orientation.VERTICAL)
        legendModifier.setLegendPosition(Gravity.START, 0, 0, 0, 10)

        // Add all those data and modifiers
        UpdateSuspender.using(surface) {
            Collections.addAll(
                surface.renderableSeries,
                twoEcgLineSeries,
                ecgLineSeries,
                isrcLineSeries,
                icgLineSeries,
                temperatureLineSeries
            )
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

        // Observe all the incoming measurements from the UDP socket
        chartViewModel.sectionAMeasurements.observe(viewLifecycleOwner) {
            // Loop through the properties in an 'A' section
            for (section in it.values) {
                // Append the values to the chart
                twoEcgLineDataSeries.append(section.tickCount, section.twoEcg)
                isrcLineDataSeries.append(section.tickCount, section.isrc)
                ecgLineDataSeries.append(section.tickCount, section.ecg)
                icgLineDataSeries.append(section.tickCount, section.icg)
                temperateLineDataSeries.append(section.tickCount, section.temperature)

                // Automatically adjust zoom depending on the values of the data
                binding.surface.zoomExtentsX()
                binding.surface.zoomExtentsY()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}