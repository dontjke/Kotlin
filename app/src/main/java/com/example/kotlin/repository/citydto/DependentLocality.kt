package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class DependentLocality(
    @SerializedName("DependentLocality")
    val dependentLocality: DependentLocalityX,
    @SerializedName("DependentLocalityName")
    val dependentLocalityName: String
)