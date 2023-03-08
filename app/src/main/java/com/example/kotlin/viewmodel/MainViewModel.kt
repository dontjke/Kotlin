package com.example.kotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.repository.Repository
import com.example.kotlin.repository.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeather() {
        Thread {
            liveData.postValue(AppState.Loading)

            if ((0..10).random() > 5) {
                val answer = repository.getWeatherFromServer()
                //TODO
                liveData.postValue(AppState.Success(answer))
            } else {
                liveData.postValue(AppState.Error(IllegalAccessException()))
            }

        }.start()

    }
}