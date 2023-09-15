package com.example.buncisapp.views.inputData

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.ShipModel
import com.example.buncisapp.data.response.FuelTypeResponse
import com.example.buncisapp.data.response.PortResponse
import com.example.buncisapp.data.response.ShipConditionResponse
import com.example.buncisapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputDataViewModel(private val pref: ShipPreference): ViewModel() {

    fun getShip(): LiveData<ShipModel> {
        return pref.getShip().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun port(token:String,callback:(List<String?>)->Unit){
        val service = ApiConfig.getApiService()
        val client = service.getPort("Bearer $token")
        client.enqueue(object : Callback<PortResponse>{
            override fun onResponse(call: Call<PortResponse>, response: Response<PortResponse>) {
                val responseBody = response.body()
                if(responseBody != null){
                    responseBody.dataPort?.port?.let { callback(it) }
                }
            }
            override fun onFailure(call: Call<PortResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
            }
        })
    }
    fun fuelType(token: String, callback: (List<String?>) -> Unit) {
        val service = ApiConfig.getApiService()
        val client = service.getFuelType("Bearer $token")
        client.enqueue(object: Callback<FuelTypeResponse> {
            override fun onResponse(call: Call<FuelTypeResponse>, response: Response<FuelTypeResponse>) {
                val responseBody = response.body()
                if(responseBody != null){
                    responseBody.data?.fuelType?.let { callback(it) }
                }
            }
            override fun onFailure(call: Call<FuelTypeResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
            }
        })
    }

    fun shipCondition(token: String, callback: (List<String?>) -> Unit){
        val service = ApiConfig.getApiService()
        val client = service.getShipCondition("Bearer $token")
        client.enqueue(object :Callback<ShipConditionResponse>{
            override fun onResponse(
                call: Call<ShipConditionResponse>,
                response: Response<ShipConditionResponse>
            ) {
                val responseBody = response.body()
                if(responseBody != null){
                    responseBody.data?.shipCondition?.let { callback(it) }
                }
            }

            override fun onFailure(call: Call<ShipConditionResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
            }
        })
    }
}