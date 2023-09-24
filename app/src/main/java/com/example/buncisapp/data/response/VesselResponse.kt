package com.example.buncisapp.data.response

import com.google.gson.annotations.SerializedName

data class VesselResponse(
    @field:SerializedName("data")
    val data: DataVessel? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int?
)

data class DataVessel(
    @field:SerializedName("vessel")
    val fuelTank: List<String?>
)