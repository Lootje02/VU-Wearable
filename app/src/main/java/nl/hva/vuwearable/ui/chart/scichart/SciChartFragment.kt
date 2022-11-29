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
import com.scichart.drawing.common.SolidPenStyle
import com.scichart.drawing.utility.ColorUtil
import nl.hva.vuwearable.databinding.FragmentSciChartBinding
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture


class SciChartFragment : Fragment() {

    private var _binding: FragmentSciChartBinding? = null

    private val chartViewModel: ChartViewModel by activityViewModels()

    private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private lateinit var schedule: ScheduledFuture<*>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val ecgLineData = DoubleValues()
    private val ecgLineDataSeries =
        XyDataSeries(Double::class.javaObjectType, Double::class.javaObjectType).apply {
            append(xValues, yValues)
        }

    private val icgLineData = DoubleValues()
    private val icgLineDataSeries =
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

        val xAxis: IAxis = NumericAxis(requireContext())
        val yAxis: IAxis = NumericAxis(requireContext())

        ecgLineDataSeries.seriesName = "ECG"

        ecgLineDataSeries.fifoCapacity = 300

        val xValues = DoubleValues()

        ecgLineDataSeries.append(xValues, ecgLineData)

        val lineSeries: IRenderableSeries = FastLineRenderableSeries()
        lineSeries.dataSeries = ecgLineDataSeries

        lineSeries.strokeStyle = SolidPenStyle(ColorUtil.LimeGreen, true, 5f, null)

        val legendModifier = LegendModifier(requireContext())
        legendModifier.setOrientation(Orientation.HORIZONTAL)
        legendModifier.setLegendPosition(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0, 0, 10)

        UpdateSuspender.using(surface) {
            Collections.addAll(surface.renderableSeries, lineSeries)
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

        var previousTime = 0.0
        chartViewModel.measurements.observe(viewLifecycleOwner) {
            for (mutableEntry in it) {
                val key = mutableEntry.key
                val value = mutableEntry.value.find { measurement ->
                    measurement.title.equals("ECG")
                }

                if (value != null) {
                    val calculatedKey = key / 1000
                    if (key != 0.0 && previousTime > key) return@observe

                    previousTime = calculatedKey

                    if (ecgLineDataSeries.xValues.contains(calculatedKey) || ecgLineDataSeries.xMax >= calculatedKey) return@observe

                    if (key != 0.0) {
                        ecgLineDataSeries.append(calculatedKey, value.value)
                    } else ecgLineDataSeries.append(ecgLineDataSeries.xMax, 0.0)
                    binding.surface.zoomExtentsX()

                }
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