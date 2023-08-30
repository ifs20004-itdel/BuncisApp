package com.example.buncisapp.data.retrofit

import com.example.buncisapp.data.response.BunkerResponse
import com.example.buncisapp.data.response.FuelTankResponse
import com.example.buncisapp.data.response.FuelTypeResponse
import com.example.buncisapp.data.response.LoginResponse
import com.example.buncisapp.data.response.PortResponse
import com.example.buncisapp.data.response.ShipConditionResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    fun login(
            @Body loginUser: RequestBody
    ): Call<LoginResponse>

    @GET("api/fuel-type")
    fun getFuelType(
        @Header("Authorization") token: String
    ): Call<FuelTypeResponse>

    @GET("api/port")
    fun getPort(
        @Header("Authorization") token: String
    ): Call<PortResponse>

    @GET("api/fuel-tank")
    fun getFuelTank(
        @Header("Authorization") token: String
    ): Call<FuelTankResponse>

    @GET("api/ship-condition")
    fun getShipCondition(
        @Header("Authorization") token: String
    ): Call<ShipConditionResponse>

    @GET("api/bunker")
    fun getBunker(
        @Header("Authorization") token: String
    ): Call<BunkerResponse>

}