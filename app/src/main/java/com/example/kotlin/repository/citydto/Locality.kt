package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class Locality(
    @SerializedName("DependentLocality")
    val dependentLocality: DependentLocality,
    @SerializedName("LocalityName")
    val localityName: String,
    @SerializedName("Premise")
    val premise: Premise,
    @SerializedName("Thoroughfare")
    val thoroughfare: Thoroughfare
)