package com.example.kotlin.viewmodel

import com.example.kotlin.repository.Weather

sealed class ResponseState {   //запечатанный класс
    object Error1 : ResponseState()
    data class Error2(val weatherList: List<Weather>) : ResponseState()
    data class Error3(val error: Throwable) : ResponseState()
}