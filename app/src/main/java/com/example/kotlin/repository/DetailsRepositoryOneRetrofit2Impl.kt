package com.example.kotlin.repository

import com.example.kotlin.BuildConfig
import com.example.kotlin.repository.dto.WeatherDTO
import com.example.kotlin.utils.YANDEX_DOMAIN
import com.example.kotlin.utils.convertDtoToModel
import com.example.kotlin.viewmodel.DetailsViewModel
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetailsRepositoryOneRetrofit2Impl : DetailsRepositoryOne {
    override fun getWeatherDetails(city: City, callbackMy: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply { //создаём инстанс Retrofit
            baseUrl(YANDEX_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)
        // weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY,city.lat,city.lon).execute() //синхронно
        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon)
            .enqueue(object : Callback<WeatherDTO> { //асинхронно
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val weather = convertDtoToModel(it)
                            weather.city = city
                            callbackMy.onResponse(weather)
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }
}