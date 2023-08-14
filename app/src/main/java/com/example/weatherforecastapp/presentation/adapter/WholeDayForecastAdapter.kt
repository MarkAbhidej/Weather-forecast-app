package com.example.weatherforecastapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.BuildConfig
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.databinding.ItemWholeDayForecastBinding
import com.example.weatherforecastapp.domain.model.wholeday.Forecast
import com.example.weatherforecastapp.util.loadImageUrl
import java.text.SimpleDateFormat
import java.util.*

class WholeDayForecastAdapter :
    RecyclerView.Adapter<WholeDayForecastAdapter.WholeDayItemViewHolder>() {

    companion object {
        private const val TIME_FORMAT = "hh a"
        private const val DATE_FORMAT = "dd MMM"
    }

    private val forecastDataList: MutableList<Forecast> = mutableListOf()

    fun updateForecastDataList(list: List<Forecast>?) {
        list?.let {
            forecastDataList.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WholeDayItemViewHolder {
        return WholeDayItemViewHolder(
            ItemWholeDayForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WholeDayItemViewHolder, position: Int) {
        holder.bind(forecastDataList[position])
    }

    override fun getItemCount(): Int {
        return forecastDataList.size
    }

    inner class WholeDayItemViewHolder(private val binding: ItemWholeDayForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Forecast) {
            binding.dayTextView.text = convertMillisToFormattedDate(data.dt)
            binding.timeTextView.text = convertMillisToFormattedTime(data.dt)
            binding.iconImageView.loadImageUrl(BuildConfig.IMAGE_URL + "/${data.weather?.first()?.icon}")
            binding.temperatureTextView.text = binding.temperatureTextView.context.getString(
                R.string.temp_celsius,
                data.main?.temp
            )
            binding.humidityTextView.text = binding.humidityTextView.context.getString(
                R.string.humidity_percentage,
                data.main?.humidity
            )
        }

        private fun convertMillisToFormattedTime(dateTime: Int?): String {
            dateTime?.let {
                val pattern = TIME_FORMAT
                val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                val date = Date(dateTime.toLong() * 1000)
                return simpleDateFormat.format(date)
            } ?: return ""
        }

        private fun convertMillisToFormattedDate(dateTime: Int?): String {
            dateTime?.let {
                val pattern = DATE_FORMAT
                val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                val date = Date(dateTime.toLong() * 1000)
                return simpleDateFormat.format(date)
            } ?: return ""
        }
    }
}