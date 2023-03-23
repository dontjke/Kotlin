package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class LocalityXX(
    @SerializedName("DependentLocality")
    val dependentLocality: DependentLocalityXXXXX,
    @SerializedName("LocalityName")
    val localityName: String,
    @SerializedName("Thoroughfare")
    val thoroughfare: Thoroughfare
)