package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class DependentLocalityX(
    @SerializedName("DependentLocality")
    val dependentLocality: DependentLocalityXX,
    @SerializedName("DependentLocalityName")
    val dependentLocalityName: String
)