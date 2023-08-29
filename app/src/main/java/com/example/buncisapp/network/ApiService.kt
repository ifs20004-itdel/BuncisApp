package com.example.buncisapp.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String
    ): Call<LoginRegisterResponse>

    @GET("api/fuel-type")
    fun getFuelType(
        @Header("Authorization") token: String
    ): Call<DetailResponse>

}