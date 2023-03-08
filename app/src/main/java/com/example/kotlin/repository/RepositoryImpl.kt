package com.example.kotlin.repository

class RepositoryImpl:Repository {
    override fun getWeatherFromServer():Weather {
        Thread.sleep(2000) //эмуляция сетевого запроса
        return Weather()
    }

    override fun getWeatherFromLocalStorage():Weather {
        Thread.sleep(20) //эмуляция локального запроса
        return Weather()
    }
}