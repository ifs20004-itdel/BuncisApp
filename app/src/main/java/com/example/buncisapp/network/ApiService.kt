package com.example.buncisapp.network

import com.example.buncisapp.response.BunkerResponse
import com.example.buncisapp.response.FuelTankResponse
import com.example.buncisapp.response.FuelTypeResponse
import com.example.buncisapp.response.LoginResponse
import com.example.buncisapp.response.PortResponse
import com.example.buncisapp.response.ShipConditionResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("bunker_code") bunker_code: String,
        @Field("password") password: String
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