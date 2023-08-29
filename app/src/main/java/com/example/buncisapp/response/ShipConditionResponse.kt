package com.example.buncisapp.response

import com.google.gson.annotations.SerializedName

data class ShipConditionResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataShipCondition(

	@field:SerializedName("ship_condition")
	val shipCondition: List<String?>? = null
)
