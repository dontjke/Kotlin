package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class Component(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("name")
    val name: String
)