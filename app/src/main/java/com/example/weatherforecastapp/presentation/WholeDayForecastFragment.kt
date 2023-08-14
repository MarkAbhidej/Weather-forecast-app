package com.example.weatherforecastapp.presentation

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.BuildConfig
import com.example.weatherforecastapp.databinding.FragmemtWholeDayForecastBinding
import com.example.weatherforecastapp.presentation.adapter.WholeDayForecastAdapter
import com.example.weatherforecastapp.util.loadImageUrl
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherforecastapp.R
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WholeDayForecastFragment : Fragment() {

    private var _binding: FragmemtWholeDayForecastBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForecastViewModel by sharedViewModel()
    private lateinit var wholeDayForecastAdapter: WholeDayForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmemtWholeDayForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeViewModel()
        viewModel.getWholeDayForecast()
    }

    private fun initRecyclerView() {
        wholeDayForecastAdapter = WholeDayForecastAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = this@WholeDayForecastFragment.wholeDayForecastAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.onShowWholeDayForecast().observe(viewLifecycleOwner) { wholeDayData ->
            with(binding) {
                titleTextView.text = getString(R.string.title_whole_day)
                tempTextView.text = getString(
                    R.string.temp_celsius,
                    wholeDayData.list?.firstOrNull()?.main?.temp
                )
                humidityTextView.text = getString(
                    R.string.humidity_percentage,
                    wholeDayData.list?.firstOrNull()?.main?.humidity
                )
                weatherTextView.text =
                    wholeDayData.list?.firstOrNull()?.weather?.firstOrNull()?.description
                countryTextView.text = wholeDayData.city?.country
                cityTextView.text = wholeDayData.city?.name
                iconImageView.loadImageUrl("${BuildConfig.IMAGE_URL}/${wholeDayData.list?.firstOrNull()?.weather?.firstOrNull()?.icon}")
                humidityImageView.visibility = View.VISIBLE
            }
            wholeDayForecastAdapter.updateForecastDataList(wholeDayData.list)
        }

        viewModel.onShowWholeDayForecastError().observe(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_error))
                .setPositiveButton(getString(R.string.ok_button)) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }
}