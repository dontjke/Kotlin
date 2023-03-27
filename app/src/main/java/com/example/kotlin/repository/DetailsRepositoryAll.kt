package com.example.kotlin.repository


import com.example.kotlin.viewmodel.HistoryViewModel


interface DetailsRepositoryAll { //фасад репозитория
    fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) //принимаем на вход координаты из любого источника (OkHttp,WeatherLoader,...)
}