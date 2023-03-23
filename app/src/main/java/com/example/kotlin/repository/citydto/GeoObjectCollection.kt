package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class GeoObjectCollection(
    @SerializedName("response")
    val response: Response
)