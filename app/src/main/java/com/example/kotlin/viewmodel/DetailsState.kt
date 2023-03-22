package com.example.kotlin.viewmodel

import com.example.kotlin.repository.Weather

sealed class DetailsState {   //запечатанный класс
    object Loading : DetailsState()
    data class Success(val weatherList: List<Weather>) : DetailsState()
    data class Error(val error: Throwable) : DetailsState()
}