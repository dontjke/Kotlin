package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class BoundedByX(
    @SerializedName("Envelope")
    val envelope: Envelope
)