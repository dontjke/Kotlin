package com.example.kotlin.repository


import com.example.kotlin.viewmodel.DetailsViewModel


interface CitiesRepository { //фасад репозитория
    fun getCityList() //принимаем на вход координаты из любого источника (OkHttp,WeatherLoader,...)
}