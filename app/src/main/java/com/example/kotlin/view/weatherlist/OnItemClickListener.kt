package com.example.kotlin.view.weatherlist

import com.example.kotlin.repository.Weather

interface OnItemClickListener {
    fun onItemClick(weather: Weather)
}