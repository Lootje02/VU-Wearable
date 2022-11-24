package nl.hva.vuwearable.ui.chart.scichart

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.scichart.charting.model.dataSeries.XyDataSeries
import com.scichart.charting.modifiers.*
import com.scichart.charting.visuals.axes.IAxis
import com.scichart.charting.visuals.axes.NumericAxis
import com.scichart.charting.visuals.pointmarkers.EllipsePointMarker
import com.scichart.charting.visuals.renderableSeries.FastLineRenderableSeries
import com.scichart.charting.visuals.renderableSeries.IRenderableSeries
import com.scichart.core.annotations.Orientation
import com.scichart.core.framework.UpdateSuspender
import com.scichart.core.model.DoubleValues
import com.scichart.core.model.IntegerValues
import com.scichart.drawing.common.SolidBrushStyle
import nl.hva.vuwearable.databinding.FragmentSciChartBinding
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class SciChartFragment : Fragment() {

    private var _binding: FragmentSciChartBinding? = null
    private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private lateinit var schedule: ScheduledFuture<*>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val lineData = DoubleValues()
    private val lineDataSeries =
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

        lineDataSeries.seriesName = "ECG"

        lineDataSeries.fifoCapacity = 300

        val xValues = IntegerValues()

        lineDataSeries.append(xValues, lineData)

        val lineSeries: IRenderableSeries = FastLineRenderableSeries()
        lineSeries.dataSeries = lineDataSeries

        val pointMarker = EllipsePointMarker()
        pointMarker.fillStyle = SolidBrushStyle(-0xcd32ce)
        pointMarker.setSize(10, 10)

        val legendModifier = LegendModifier(requireContext())
        legendModifier.setOrientation(Orientation.HORIZONTAL)
        legendModifier.setLegendPosition(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0, 0, 10)

        UpdateSuspender.using(surface) {
            Collections.addAll(surface.renderableSeries, lineSeries)
            Collections.addAll(
                surface.chartModifiers,
                PinchZoomModifier(),
                ZoomPanModifier(),
                ZoomExtentsModifier()
            )
            Collections.addAll(surface.chartModifiers, legendModifier)
            Collections.addAll(surface.chartModifiers, RolloverModifier())
        }

        UpdateSuspender.using(surface) {
            Collections.addAll(surface.xAxes, xAxis)
            Collections.addAll(surface.yAxes, yAxis)
        }

        schedule = scheduledExecutorService.scheduleWithFixedDelay(
            updateData,
            0,
            15,
            TimeUnit.MILLISECONDS
        )

        return binding.root
    }

    private var count = 0
    private var changeCount = 0

    private val updateData = Runnable {
        val x = count
        UpdateSuspender.using(binding.surface) {
            var totalStepsDone = -1
            if (changeCount == 100) {
                for (i in 0..10) {
                    lineDataSeries.append(x + ++totalStepsDone, 0.01 * i)
                }

                Log.i("UDP", totalStepsDone.toString())
                lineDataSeries.append(x + ++totalStepsDone, 0.09)
                Log.i("UDP", totalStepsDone.toString())

                lineDataSeries.append(x + ++totalStepsDone, 0.08)
                lineDataSeries.append(x + ++totalStepsDone, 0.07)
                lineDataSeries.append(x + ++totalStepsDone, 0.06)
                lineDataSeries.append(x + ++totalStepsDone, 0.05)
                lineDataSeries.append(x + ++totalStepsDone, 0.04)
                lineDataSeries.append(x + ++totalStepsDone, 0.03)
                lineDataSeries.append(x + ++totalStepsDone, 0.02)
                lineDataSeries.append(x + ++totalStepsDone, 0.01)

                for (i in 0..5) {
                    lineDataSeries.append(x + ++totalStepsDone, -0.01 * i)
                }

                for (i in 0..30) {
                    lineDataSeries.append(x + ++totalStepsDone, 0.0)
                }


                for (i in 0..10) {
                    lineDataSeries.append(x + ++totalStepsDone, 0.02 * i)
                }

                for (i in 10 downTo -6) {
                    lineDataSeries.append(x + ++totalStepsDone, 0.02 * i)
                }

                for (i in -6..0) {
                    lineDataSeries.append(x + ++totalStepsDone, 0.02 * i)
                }

                count += totalStepsDone
                changeCount = 0
                totalStepsDone = 0
            } else {
                lineDataSeries.append(x, 0.0)
            }

            binding.surface.zoomExtentsX()
            count++
            changeCount++
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}