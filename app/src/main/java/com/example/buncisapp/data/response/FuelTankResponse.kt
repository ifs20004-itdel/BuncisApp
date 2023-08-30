package com.example.buncisapp.data.response

import com.google.gson.annotations.SerializedName

data class DataFuelTank(

	@field:SerializedName("fuel_tank")
	val fuelTank: List<String?>? = null
)

data class FuelTankResponse(

    @field:SerializedName("dataFuelTank")
	val data: DataFuelTank? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Int? = null
)
