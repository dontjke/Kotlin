package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class LocalityX(
    @SerializedName("DependentLocality")
    val dependentLocality: DependentLocalityXXX,
    @SerializedName("LocalityName")
    val localityName: String,
    @SerializedName("Premise")
    val premise: PremiseX,
    @SerializedName("Thoroughfare")
    val thoroughfare: Thoroughfare
)