package com.example.buncisapp.data.response

import com.google.gson.annotations.SerializedName

data class CalculationResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("volume")
	val volume: Any? = null,

	@field:SerializedName("level_sounding")
	val levelSounding: Int? = null,

	@field:SerializedName("trim")
	val trim: Any? = null,

	@field:SerializedName("nomor_tanki")
	val nomorTanki: String? = null,

	@field:SerializedName("heel_correction")
	val heelCorrection: Any? = null
)
