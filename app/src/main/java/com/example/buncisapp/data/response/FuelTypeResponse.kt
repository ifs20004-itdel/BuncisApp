package com.example.buncisapp.data.response

import com.google.gson.annotations.SerializedName

data class FuelTypeResponse(

    @field:SerializedName("data")
    val data: DataFuelType? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class DataFuelType(

    @field:SerializedName("fuel_type")
    val fuelType: List<String?>
)
