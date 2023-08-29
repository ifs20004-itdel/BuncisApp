package com.example.buncisapp.response

import com.google.gson.annotations.SerializedName

data class FuelTypeResponse(

    @field:SerializedName("data")
	val data: Data? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Int? = null
)

data class Data(

	@field:SerializedName("fuel_type")
	val fuelType: List<String?>? = null
)
