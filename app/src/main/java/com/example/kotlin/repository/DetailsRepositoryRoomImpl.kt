package com.example.kotlin.repository

import com.example.kotlin.MyApp
import com.example.kotlin.utils.convertHistoryEntityToWeather
import com.example.kotlin.utils.convertWeatherToEntity
import com.example.kotlin.viewmodel.DetailsViewModel
import com.example.kotlin.viewmodel.HistoryViewModel


class DetailsRepositoryRoomImpl : DetailsRepositoryOne, DetailsRepositoryAll, DetailsRepositoryAdd {
    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {
        Thread {
            callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDao().getAll()))
        }.start()
    }

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        Thread {
            val list =
                convertHistoryEntityToWeather(MyApp.getHistoryDao().getHistoryForCity(city.name))
            callback.onResponse(list.last())
        }.start()
    }
    override fun addWeather(weather: Weather) {
        Thread {

            MyApp.getHistoryDao().insert(convertWeatherToEntity(weather))
        }.start()
    }
}