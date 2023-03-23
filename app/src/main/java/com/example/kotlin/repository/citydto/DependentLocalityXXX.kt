package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class DependentLocalityXXX(
    @SerializedName("DependentLocality")
    val dependentLocality: DependentLocality,
    @SerializedName("DependentLocalityName")
    val dependentLocalityName: String
)