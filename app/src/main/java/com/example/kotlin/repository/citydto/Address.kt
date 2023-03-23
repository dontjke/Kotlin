package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("Components")
    val components: List<Component>,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("formatted")
    val formatted: String
)