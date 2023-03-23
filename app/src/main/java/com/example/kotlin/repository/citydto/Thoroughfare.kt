package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class Thoroughfare(
    @SerializedName("ThoroughfareName")
    val thoroughfareName: String
)