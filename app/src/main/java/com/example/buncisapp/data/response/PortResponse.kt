package com.example.buncisapp.data.response

import com.google.gson.annotations.SerializedName

data class PortResponse(

    @field:SerializedName("data")
    val dataPort: DataPort? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class DataPort(

    @field:SerializedName("port")
    val port: List<String?>
)
