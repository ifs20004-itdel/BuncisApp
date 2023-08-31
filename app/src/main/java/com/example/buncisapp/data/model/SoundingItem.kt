package com.example.buncisapp.data.model

import com.google.gson.annotations.SerializedName

data class SoundingItem(

	@field:SerializedName("level_sounding")
	val levelSounding: Int? = null,

	@field:SerializedName("nomor_tanki")
	val nomorTanki: String? = null
)