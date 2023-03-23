package com.example.kotlin.repository

import com.example.kotlin.repository.dto.WeatherDTO
import com.example.kotlin.utils.KEY_BUNDLE_LAT
import com.example.kotlin.utils.KEY_BUNDLE_LON
import com.example.kotlin.utils.YANDEX_API_KEY
import com.example.kotlin.utils.YANDEX_ENDPOINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {
    @GET(YANDEX_ENDPOINT) //только endpoint
    fun getWeather(
        @Header(YANDEX_API_KEY) apikey: String,
        @Query(KEY_BUNDLE_LAT) lat: Double,
        @Query(KEY_BUNDLE_LON) lon: Double
    ): Call<WeatherDTO>
}