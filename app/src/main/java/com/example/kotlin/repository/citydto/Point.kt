package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class Point(
    @SerializedName("pos")
    val pos: String
)