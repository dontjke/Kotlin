package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("GeoObjectCollection")
    val geoObjectCollection: GeoObjectCollectionX
)