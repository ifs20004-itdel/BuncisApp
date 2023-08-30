//package com.example.buncisapp.di
//
//import android.content.Context
//import com.example.buncisapp.data.retrofit.ApiConfig
//
//object Injection {
//    fun provideRepository(context: Context): ShipRepository {
//        val database = ShipDatabase.getDatabase(context)
//        val apiService = ApiConfig.getApiService()
//        return StoryRepository(database, apiService)
//    }
//}