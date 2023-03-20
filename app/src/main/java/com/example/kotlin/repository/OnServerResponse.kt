package com.example.kotlin.repository

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}