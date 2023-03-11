package com.example.kotlin.viewmodel

import com.example.kotlin.repository.Weather

sealed class AppState {   //запечатанный класс
    object Loading : AppState()
    data class Success(val weatherList: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
}