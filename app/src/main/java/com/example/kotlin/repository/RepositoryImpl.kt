package com.example.kotlin.repository

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(2000) //эмуляция сетевого запроса
        return Weather()
    }
    override fun getWeatherFromLocalStorageRus() = getRussianCities() // эмуляция ответа

    override fun getWeatherFromLocalStorageWorld() = getWorldCities() // эмуляция ответа
}

