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
import com.scichart.core.model.IntegerValues
import com.scichart.data.model.DoubleRange
import com.scichart.drawing.common.SolidPenStyle
import com.scichart.drawing.utility.ColorUtil
import nl.hva.vuwearable.databinding.FragmentSciChartBinding
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture

/**
 * Initializes and updates the chart in real time.
 *
 * @author Bunyamin Duduk
 */
class SciChartFragment : Fragment() {

    private var _binding: FragmentSciChartBinding? = null

    private val chartViewModel: ChartViewModel by activityViewModels()

    private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private lateinit var schedule: ScheduledFuture<*>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val ecgLineData = IntegerValues()
    private val ecgLineDataSeries =
        XyDataSeries(Int::class.javaObjectType, Int::class.javaObjectType).apply {
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

        val xAxis: IAxis = NumericAxis(requireContext())
        val yAxis: IAxis = NumericAxis(requireContext())

        ecgLineDataSeries.seriesName = "ECG"
        icgLineDataSeries.seriesName = "ICG"

        ecgLineDataSeries.fifoCapacity = 10000
        icgLineDataSeries.fifoCapacity = 10000000

        yAxis.growBy = DoubleRange(0.4, 0.4)

        val xValues = IntegerValues()

        ecgLineDataSeries.append(xValues, ecgLineData)
        icgLineDataSeries.append(xValues, icgLineData)

        val ecgLineSeries: IRenderableSeries = FastLineRenderableSeries()
        ecgLineSeries.dataSeries = ecgLineDataSeries

        val icgLineSeries: IRenderableSeries = FastLineRenderableSeries()
        icgLineSeries.dataSeries = icgLineDataSeries


        ecgLineSeries.strokeStyle = SolidPenStyle(ColorUtil.LimeGreen, true, 5f, null)
        icgLineSeries.strokeStyle = SolidPenStyle(ColorUtil.Yellow, true, 5f, null)

        val legendModifier = LegendModifier(requireContext())
        legendModifier.setOrientation(Orientation.HORIZONTAL)
        legendModifier.setLegendPosition(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0, 0, 10)

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

        UpdateSuspender.using(surface) {
            Collections.addAll(surface.xAxes, xAxis)
            Collections.addAll(surface.yAxes, yAxis)
        }

        // Append the first value
        ecgLineDataSeries.append(0, 0)
        icgLineDataSeries.append(0, 0)

        chartViewModel.measurements.observe(viewLifecycleOwner) {
            for (mutableEntry in it) {
                val key = mutableEntry.key
                val ecgValue = mutableEntry.value.find { measurement ->
                    measurement.title == "ECG"
                }

                if (ecgValue != null) {
                    if (ecgLineDataSeries.xValues.size > 0 && ecgLineDataSeries.xValues.last() < key && !ecgLineDataSeries.xValues.contains(
                            key.toInt() / 10000
                        )
                    )
                        ecgLineDataSeries.append(key, ecgValue.value)
                }

                val icgValue = mutableEntry.value.find { measurement ->
                    measurement.title == "ICG"
                }

                if (icgValue != null) {
                    if (icgLineDataSeries.xValues.size > 0 && icgLineDataSeries.xValues.last() < key && !icgLineDataSeries.xValues.contains(
                            key / 10000
                        )
                    )
                        icgLineDataSeries.append(key, icgValue.value)
                }

                binding.surface.zoomExtentsX()
                binding.surface.zoomExtentsY()

            }
        }

//        schedule = scheduledExecutorService.scheduleWithFixedDelay(
//            updateData,
//            0,
//            15,
//            TimeUnit.MILLISECONDS
//        )

//        view

        return binding.root
    }

    private var count = 0
    private var changeCount = 0

//    private val updateData = Runnable {
//        val x = count
//        UpdateSuspender.using(binding.surface) {
//            var totalStepsDone = -1
//            if (changeCount == 100) {
//                for (i in 0..10) {
//                    ecgLineDataSeries.append(x + ++totalStepsDone, 0.01 * i)
//                }
//
//                Log.i("UDP", totalStepsDone.toString())
//                ecgLineDataSeries.append(x + ++totalStepsDone, 0.09)
//                Log.i("UDP", totalStepsDone.toString())
//
//                ecgLineDataSeries.append(x + ++totalStepsDone, 0.08)
//                ecgLineDataSeries.append(x + ++totalStepsDone, 0.07)
//                ecgLineDataSeries.append(x + ++totalStepsDone, 0.06)
//                ecgLineDataSeries.append(x + ++totalStepsDone, 0.05)
//                ecgLineDataSeries.append(x + ++totalStepsDone, 0.04)
//                ecgLineDataSeries.append(x + ++totalStepsDone, 0.03)
//                ecgLineDataSeries.append(x + ++totalStepsDone, 0.02)
//                ecgLineDataSeries.append(x + ++totalStepsDone, 0.01)
//
//                for (i in 0..5) {
//                    ecgLineDataSeries.append(x + ++totalStepsDone, -0.01 * i)
//                }
//
//                for (i in 0..30) {
//                    ecgLineDataSeries.append(x + ++totalStepsDone, 0.0)
//                }
//
//
//                for (i in 0..10) {
//                    ecgLineDataSeries.append(x + ++totalStepsDone, 0.02 * i)
//                }
//
//                for (i in 10 downTo -6) {
//                    ecgLineDataSeries.append(x + ++totalStepsDone, 0.02 * i)
//                }
//
//                for (i in -6..0) {
//                    ecgLineDataSeries.append(x + ++totalStepsDone, 0.02 * i)
//                }
//
//                count += totalStepsDone
//                changeCount = 0
//                totalStepsDone = 0
//            } else {
//                ecgLineDataSeries.append(x, 0.0)
//            }
//
//            binding.surface.zoomExtentsX()
//            count++
//            changeCount++
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}