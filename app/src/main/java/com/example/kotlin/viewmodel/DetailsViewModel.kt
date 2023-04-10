package com.example.kotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repositoryOne: DetailsRepositoryOne = DetailsRepositoryOneRetrofit2Impl(),/*DetailsRepositoryOkhttpImpl()*/ //можно подключить любой репозиторий, который реализует DetailsRepository
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImpl()
) : ViewModel() {

    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsState.Loading)
        repositoryOne.getWeatherDetails(
            city
        ) { weather ->
            liveData.postValue(DetailsState.Success(weather))
            repositoryAdd.addWeather(weather)
        }
    }

    fun interface Callback {
        fun onResponse(weather: Weather)
    }

}