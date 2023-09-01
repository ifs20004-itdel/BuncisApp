package com.example.buncisapp.data.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class SoundingItem(

	@field:SerializedName("nomor_tanki")
	val nomorTanki: String? = null,

	@field:SerializedName("level_sounding")
	val levelSounding: Double? = null
)

data class SoundingItems(

	@SerializedName("nomor_tanki")
	val nomorTanki: String,

	@SerializedName("level_sounding")
	val levelSounding: Double
)