package nl.hva.vuwearable.ui.breathing

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
import nl.hva.vuwearable.databinding.FragmentBreathingExcerciseBinding
import nl.hva.vuwearable.ui.chart.scichart.ChartViewModel
import java.util.*


class BreathingExcerciseFragment : Fragment() {

    private var _binding: FragmentBreathingExcerciseBinding? = null

    private val binding get() = _binding!!

    private val breathingViewModel: BreathingViewModel by activityViewModels()

    private val chartViewModel: ChartViewModel by activityViewModels()

    private val icgLineData = DoubleValues()

    private val icgLineDataSeries =
        XyDataSeries(Int::class.javaObjectType, Double::class.javaObjectType).apply {
            append(xValues, yValues)
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBreathingExcerciseBinding.inflate(inflater, container, false)

        Log.i("EXCERCISE", breathingViewModel.breatheIn.value.toString())

        startAnimation()

        val surface = binding.surface

        // Create x and y axis
        val xAxis: IAxis = NumericAxis(requireContext())
        val yAxis: IAxis = NumericAxis(requireContext())

        // Name of the line
        icgLineDataSeries.seriesName = "ICG"

        // How much it will show on the screen
        icgLineDataSeries.fifoCapacity = 5000

        // Add some padding at the bottom and top to have a more clear view
        yAxis.growBy = DoubleRange(0.3, 0.3)

        val xValues = IntegerValues()

        // Append data to initialise the data series
        icgLineDataSeries.append(xValues, icgLineData)

        // Type of line
        val icgLineSeries: IRenderableSeries = FastLineRenderableSeries()
        icgLineSeries.dataSeries = icgLineDataSeries

        // Color of the line
        icgLineSeries.strokeStyle = SolidPenStyle(ColorUtil.Yellow, true, 5f, null)

        // Show in a box what the lines are
        val legendModifier = LegendModifier(requireContext())
        legendModifier.setOrientation(Orientation.HORIZONTAL)
        legendModifier.setLegendPosition(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0, 0, 10)

        // Add all those data and modifiers
        UpdateSuspender.using(surface) {
            Collections.addAll(surface.renderableSeries, icgLineSeries)
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
                icgLineDataSeries.append(section.tickCount, section.icg)

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

    private fun startAnimation() {
        val animator = binding.viewAnimator

        animator.animate()
            .setDuration(1000)
            .

//        when (breathingViewModel.breatheIn.value) {
//            1 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_1sec))
//            2 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_2sec))
//            3 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_3sec))
//            4 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_4sec))
//            5 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_5sec))
//            6 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_6sec))
//            7 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_7sec))
//            8 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_8sec))
//            9 -> animator.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in_9sec))
//            10 -> animator.startAnimation(
//                AnimationUtils.loadAnimation(
//                    context,
//                    R.anim.zoom_in_10sec
//                )
//            )
//        }
    }


}