package com.example.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryRetrofit2Impl()/*DetailsRepositoryOkhttpImpl()*/ //можно подключить любой репозиторий, который реализует DetailsRepository
) : ViewModel() {

    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsState.Loading)
        repository.getWeatherDetails(
            city
        ) { weather -> liveData.postValue(DetailsState.Success(weather)) }
    }

    fun interface Callback {
        fun onResponse(weather: Weather)
    }
}