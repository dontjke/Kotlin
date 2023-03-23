package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class Premise(
    @SerializedName("PremiseName")
    val premiseName: String
)