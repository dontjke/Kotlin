package com.example.kotlin.repository

import com.example.kotlin.repository.dto.WeatherDTO

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}