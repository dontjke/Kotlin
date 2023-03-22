package com.example.kotlin.repository

import com.example.kotlin.repository.dto.WeatherDTO


interface DetailsRepository {
    fun getWeatherDetails(lat: Double, lon: Double):WeatherDTO   //принимаем на вход координаты из любого источника (OkHttp,urlConnection,...) возвращаем WeatherDTO
}