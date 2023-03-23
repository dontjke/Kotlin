package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class BoundedBy(
    @SerializedName("Envelope")
    val envelope: Envelope
)