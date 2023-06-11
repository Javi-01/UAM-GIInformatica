package es.uam.eps.dadm.cardsjavier

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.uam.eps.dadm.cardsjavier.card.Card
import es.uam.eps.dadm.cardsjavier.card.StatisticsViewModel
import es.uam.eps.dadm.cardsjavier.databinding.FragmentStatisticsBinding
import es.uam.eps.dadm.cardsjavier.deck.Deck
import es.uam.eps.dadm.cardsjavier.deck.DeckListViewModel
import es.uam.eps.dadm.cardsjavier.deck.DeckStatsAdapter
import es.uam.eps.dadm.cardsjavier.deck.DeckWithCards
import java.util.*


class StatisticsFragment : Fragment() {

    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var adapter: DeckStatsAdapter

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    private var chartInfo: MutableList<Int> = mutableListOf()
    private var currentDeck: DeckWithCards? = null
    private val emptyList: List<Card> = emptyList()


    private lateinit var statisticsViewModel: StatisticsViewModel
    private val deckListViewModel: DeckListViewModel by lazy {
        ViewModelProvider(this)[DeckListViewModel::class.java]
    }
    private lateinit var auth: FirebaseAuth

    private var qualityChart = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        binding.deckListRc.apply {
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
        adapter = DeckStatsAdapter(requireActivity())
        adapter.decks = mutableListOf()
        binding.deckListRc.adapter = adapter
        binding.deckWithCards = DeckWithCards(Deck(423423, "Todos", ""), emptyList)

        auth = Firebase.auth

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticsViewModel = ViewModelProvider(requireActivity())[StatisticsViewModel::class.java]

        binding.nextPracticeBtn.setOnClickListener {
            qualityChart = false
            populateChart(currentDeck)
        }

        binding.qualityBtn.setOnClickListener {
            qualityChart = true
            populateChart(currentDeck)
        }

        deckListViewModel.loadUserId(auth.uid!!)

        deckListViewModel.decks.observe(viewLifecycleOwner) {
            adapter.decks = it.toMutableList()
            populateChart()
            adapter.notifyDataSetChanged()
        }

        statisticsViewModel.deck.observe(viewLifecycleOwner) {

            // Si pulsamos sobre distinto deck, actualizamos
            currentDeck = if (currentDeck?.deck?.deckId != it.deck.deckId){
                populateChart(it)
                it
                // Si pulsamos sobre el mismo deck, actulizamos con todos los decks
            }else{
                populateChart()
                null
            }
            binding.deckWithCards = currentDeck?: DeckWithCards(Deck(423423, "Todos", ""), emptyList)
        }
    }

    private fun populateChart(d: DeckWithCards? = null) {
        chartInfo.clear()

        if (d != null) {
            if (qualityChart) {
                chartInfo.addAll(statisticsViewModel.deckQuality(d))
                updatePieChart()
            }else{
                chartInfo.addAll(statisticsViewModel.deckNextPractices(d))
                updateBarChart()
            }
        }else {
            if (qualityChart) {
                for (deck in adapter.decks){
                    chartInfo.addAll(statisticsViewModel.deckQuality(deck))
                }
                updatePieChart()
            }else{
                for (deck in adapter.decks){
                    chartInfo.addAll(statisticsViewModel.deckNextPractices(deck))
                }
                updateBarChart()
            }
        }
    }

    private fun frequency(): Map<String, Float> {
        val entries: MutableMap<String, Float> = mutableMapOf()
        for (ele in chartInfo.distinct().sorted()) {
            val str = when (ele) {
                0 -> "Dificil"
                3 -> "Duda"
                5 -> "Facil"
                else -> ""
            }
            entries[str] = (Collections.frequency(chartInfo, ele) * 100 / chartInfo.size).toFloat()
        }
        return entries
    }

    private fun nextPractice(): Map<String, Float> {
        val entries: MutableMap<String, Float> = mutableMapOf()
        for (ele in chartInfo.distinct().sorted()) {
            entries[ele.toString()] = (Collections.frequency(chartInfo, ele)).toFloat()
        }
        return entries
    }

    private fun updatePieChart() {
        // Create the Chart
        pieChart = binding.pieChart
        pieChart.setUsePercentValues(true)

        val desc= Description()
        desc.text = ""
        pieChart.description = desc


        val legend: Legend? = pieChart.legend
        legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend?.xEntrySpace = 60f
        legend?.xOffset = 28f
        legend?.textSize = 18f

        val values = frequency()
        val colors = mutableListOf<Int>()

        val entries: MutableList<PieEntry> = mutableListOf()
        for (i in values) {
            when(i.key){
                "Dificil" -> colors.add(Color.RED)
                "Duda" -> colors.add(Color.BLUE)
                "Facil" -> colors.add(Color.GREEN)
            }
            entries.add(PieEntry(i.value, i.key))
        }
        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawValues(true)

        dataSet.colors = colors

        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(18f)
        pieData.setValueTextColor(Color.WHITE)

        pieChart.data = pieData
        binding.barChart.visibility = View.GONE
        binding.pieChart.visibility = View.VISIBLE
        pieChart.invalidate()
    }

    private fun updateBarChart() {
        // Create the Chart
        barChart = binding.barChart
        barChart.animateY(500)
        barChart.description.isEnabled = false
        barChart.isDragEnabled = false
        barChart.setFitBars(true)
        barChart.setTouchEnabled(false)
        barChart.isDoubleTapToZoomEnabled = false


        // Ocultar el eje Y
        barChart.axisLeft.setDrawAxisLine(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisLeft.setDrawLabels(false)
        barChart.axisRight.setDrawAxisLine(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.axisRight.setDrawLabels(false)

        // Mostrar solo el eje X
        barChart.xAxis.setDrawAxisLine(true)
        barChart.xAxis.setDrawGridLines(true)

        val values = nextPractice()
        val labels = mutableListOf<String>()

        val entries: MutableList<BarEntry> = mutableListOf()
        for (i in values) {
            entries.add(BarEntry(i.key.toFloat(), i.value))
            labels.add(i.key)
        }

        val barDataSet = BarDataSet(entries, "Number Cards/Days until Next Review")
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 15f


        val barData = BarData(barDataSet)
        barDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS, 250)

        barData.barWidth = 0.9f
        barChart.data = barData

        binding.pieChart.visibility = View.GONE
        binding.barChart.visibility = View.VISIBLE
        barChart.invalidate()
    }
}