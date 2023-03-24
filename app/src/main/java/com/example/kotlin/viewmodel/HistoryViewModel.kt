package com.example.kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.repository.*


class HistoryViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: DetailsRepositoryRoomImpl = DetailsRepositoryRoomImpl()

) : ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }
    fun getAll(){
        repository.getAllWeatherDetails(object :CallbackForAll {
            override fun onResponse(listWeather: List<Weather>) {
                liveData.postValue(AppState.Success(listWeather))
            }

        })
    }

    fun interface CallbackForAll {
        fun onResponse(listWeather: List<Weather>)
    }
   

}