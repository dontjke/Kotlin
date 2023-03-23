package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class FeatureMember(
    @SerializedName("GeoObject")
    val geoObject: GeoObject
)