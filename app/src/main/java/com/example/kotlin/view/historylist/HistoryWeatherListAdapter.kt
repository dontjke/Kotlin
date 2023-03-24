package com.example.kotlin.view.weatherlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.databinding.FragmentHistoryWeatherListRecyclerItemBinding
import com.example.kotlin.repository.Weather
import com.example.kotlin.utils.loadSvg

class HistoryWeatherListAdapter(
    private var data: List<Weather> = listOf()
) :
    RecyclerView.Adapter<HistoryWeatherListAdapter.CityHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataNew: List<Weather>) {
        this.data = dataNew
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binding = FragmentHistoryWeatherListRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CityHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount() = data.size

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) {
            FragmentHistoryWeatherListRecyclerItemBinding.bind(itemView).apply {
                cityNameTextView.text = weather.city.name
                temperatureTextView.text = weather.temperature.toString()
                feelsLikeTextView.text = weather.feelsLike.toString()
                icon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")

            }
        }


    }

}