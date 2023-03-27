package com.example.kotlin.repository

import com.example.kotlin.MyApp
import com.example.kotlin.utils.convertHistoryEntityToWeather
import com.example.kotlin.utils.convertWeatherToEntity
import com.example.kotlin.viewmodel.DetailsViewModel
import com.example.kotlin.viewmodel.HistoryViewModel


class DetailsRepositoryRoomImpl : DetailsRepositoryOne,DetailsRepositoryAll,DetailsRepositoryAdd {
    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {
        callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDao().getAll()))
    }

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val list = convertHistoryEntityToWeather(MyApp.getHistoryDao().getHistoryForCity(city.name))
        callback.onResponse(list.last())
    }

    override fun addWeather(weather: Weather) {
        MyApp.getHistoryDao().insert(convertWeatherToEntity(weather))
    }

}