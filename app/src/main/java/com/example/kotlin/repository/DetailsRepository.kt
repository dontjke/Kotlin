package com.example.kotlin.repository


import com.example.kotlin.viewmodel.DetailsViewModel


interface DetailsRepository { //фасад репозитория
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) //принимаем на вход координаты из любого источника (OkHttp,WeatherLoader,...)
}