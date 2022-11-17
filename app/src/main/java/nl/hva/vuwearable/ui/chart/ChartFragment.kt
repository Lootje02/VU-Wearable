package nl.hva.vuwearable.ui.chart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import nl.hva.vuwearable.databinding.FragmentChartBinding

class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ChartViewModel::class.java)

        _binding = FragmentChartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupLineChart()
        setDataToLines()

        return root
    }

    private fun setupLineChart() {
        with(binding.lineChart) {
            animateX(1200, Easing.EaseInSine)
            description.isEnabled = false
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            axisLeft.mAxisMinimum = 0f

            axisRight.isEnabled = false
            extraRightOffset = 30f

            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.textSize = 15F
            legend.form = Legend.LegendForm.LINE
        }
    }

    private fun setDataToLines() {
        val first = LineDataSet(arrayListOf(), "BPM")
        first.apply {
            lineWidth = 3f
        }

        val dataSet = mutableListOf<ILineDataSet>()
        dataSet.add(first)

        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData


        handler = Handler(Looper.getMainLooper())

        runnable = Runnable {
            val data = binding.lineChart.data
            val set = data.getDataSetByIndex(0)

            data.addEntry(
                Entry(set.entryCount.toFloat(), (0..100).shuffled().last().toFloat()),
                0
            )
            data.notifyDataChanged()

            // To reset the graph every 1000ms
            // binding.lineChart.animateX(1000, Easing.Linear)

            binding.lineChart.notifyDataSetChanged()


            binding.lineChart.setVisibleXRangeMaximum(10f);
            binding.lineChart.moveViewToX(data.entryCount.toFloat())

            handler.postDelayed(runnable, 250)
        }

        handler.postDelayed(runnable, 100)

        binding.lineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
        _binding = null
    }
}