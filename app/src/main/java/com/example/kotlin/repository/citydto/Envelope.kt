package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class Envelope(
    @SerializedName("lowerCorner")
    val lowerCorner: String,
    @SerializedName("upperCorner")
    val upperCorner: String
)