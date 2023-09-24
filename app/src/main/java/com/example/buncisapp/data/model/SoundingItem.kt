package com.example.buncisapp.data.model

import com.google.gson.annotations.SerializedName

data class SoundingItem(

    @field:SerializedName("nomor_tanki")
    val nomorTanki: String? = null,

    @field:SerializedName("level_sounding")
    val levelSounding: Double? = null,

    @field:SerializedName("volume")
    val volume: Double? = null
)

data class SoundingItems(

    @SerializedName("nomor_tanki")
    val nomorTanki: String,

    @SerializedName("level_sounding")
    val levelSounding: Double,

    @field:SerializedName("volume")
    val volume: Double? = null
)