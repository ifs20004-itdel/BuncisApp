package com.example.buncisapp.response

import com.google.gson.annotations.SerializedName

data class FuelTypeResponse(

    @field:SerializedName("dataFuelType")
	val data: DataFuelType? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Int? = null
)

data class DataFuelType(

	@field:SerializedName("fuel_type")
	val fuelType: List<String?>? = null
)
