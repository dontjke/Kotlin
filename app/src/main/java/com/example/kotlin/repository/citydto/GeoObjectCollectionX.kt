package com.example.kotlin.repository.citydto


import com.google.gson.annotations.SerializedName

data class GeoObjectCollectionX(
    @SerializedName("featureMember")
    val featureMember: List<FeatureMember>,
    @SerializedName("metaDataProperty")
    val metaDataProperty: MetaDataPropertyX
)