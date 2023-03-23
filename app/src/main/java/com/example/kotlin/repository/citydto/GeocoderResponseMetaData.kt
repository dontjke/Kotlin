package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class GeocoderResponseMetaData(
    @SerializedName("boundedBy")
    val boundedBy: BoundedByX,
    @SerializedName("found")
    val found: String,
    @SerializedName("request")
    val request: String,
    @SerializedName("results")
    val results: String
)