package com.example.buncisapp.response

import com.google.gson.annotations.SerializedName

data class DataPort(

	@field:SerializedName("port")
	val port: List<String?>? = null
)

data class PortResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
