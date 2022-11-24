package nl.hva.vuwearable.ui.chart.scichart

import android.os.Bundle
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
import java.lang.Math.sin
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
        for (i in 0 until 200) {
            xValues.add(i)
            lineData.add(sin(i * 0.1))
            count++
        }

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
            10,
            TimeUnit.MILLISECONDS
        )

        return binding.root
    }

    private var count = 0

    private val updateData = Runnable {
        val x = count
        UpdateSuspender.using(binding.surface) {
            lineDataSeries.append(x, sin(x * 0.1))

            binding.surface.zoomExtentsX()
            count++
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val FIFO_CAPACITY = 7850
    }
}