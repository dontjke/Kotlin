package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class AdministrativeArea(
    @SerializedName("AdministrativeAreaName")
    val administrativeAreaName: String,
    @SerializedName("Locality")
    val locality: Locality,
    @SerializedName("SubAdministrativeArea")
    val subAdministrativeArea: SubAdministrativeArea
)