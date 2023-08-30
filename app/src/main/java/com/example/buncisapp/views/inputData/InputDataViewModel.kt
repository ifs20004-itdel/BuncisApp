package com.example.buncisapp.views.inputData

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.buncisapp.data.ShipModel
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.network.ApiConfig
import com.example.buncisapp.response.DataFuelType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputDataViewModel(private val pref: ShipPreference): ViewModel() {

    fun getShip(): LiveData<ShipModel> {
        return pref.getShip().asLiveData()
    }
}