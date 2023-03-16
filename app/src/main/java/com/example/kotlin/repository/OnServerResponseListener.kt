package com.example.kotlin.repository

import com.example.kotlin.viewmodel.ResponseState

fun interface OnServerResponseListener {
    fun onError(error: ResponseState)
}