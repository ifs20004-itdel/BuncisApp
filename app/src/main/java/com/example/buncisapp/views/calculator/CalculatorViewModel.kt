package com.example.buncisapp.views.calculator

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.ShipModel
import com.example.buncisapp.data.model.SoundingItems
import com.example.buncisapp.data.response.CalculationResponse
import com.example.buncisapp.data.response.FuelTankResponse
import com.example.buncisapp.data.response.RobResponse
import com.example.buncisapp.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalculatorViewModel(private val pref: ShipPreference): ViewModel() {

    private val _noTanki = MutableLiveData<List<String?>>()
    val noTanki: LiveData<List<String?>> = _noTanki

    private val _data = MutableLiveData<RobResponse>()
    val data : LiveData<RobResponse> = _data

    private val _calculation = MutableLiveData<CalculationResponse>()
    val calculation : LiveData<CalculationResponse> = _calculation

    fun getShip(): LiveData<ShipModel> {
        return pref.getShip().asLiveData()
    }

    fun noTanki(token: String) {
        val service = ApiConfig.getApiService()
        val client = service.getFuelTank("Bearer $token")
        client.enqueue(object: Callback<FuelTankResponse> {
            override fun onResponse(call: Call<FuelTankResponse>, response: Response<FuelTankResponse>) {
                val responseBody = response.body()
                if(responseBody != null){
                    _noTanki.value = responseBody.data?.fuelTank
                }
            }
            override fun onFailure(call: Call<FuelTankResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
            }
        })
    }

    fun calculation(
        token: String,
        trim: Double,
        nomor_tanki: String,
        level_sounding: Int,
        volume: Double
    ){
        val apiService = ApiConfig.getApiService()
        val json = """
            {
              "heel_correction": 0.0,
              "trim": ${trim},
              "nomor_tanki": "${nomor_tanki}",
              "level_sounding":${level_sounding},
              "volume":${volume}
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val client = apiService.calculation("Bearer $token",body )
        client.enqueue(object: Callback<CalculationResponse> {
            override fun onResponse(call: Call<CalculationResponse>, response: Response<CalculationResponse>) {
                val responseBody = response.body()
                if(response.isSuccessful){
                    if(responseBody != null ){
                        _calculation.value = response.body()
                    }
                }else{
                    Log.e("test",response.errorBody().toString())
                }
            }
            override fun onFailure(call: Call<CalculationResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
            }

        })
    }

    fun postResult(
        token: String,
        pelabuhan: String, tanggal:String,
        waktu: String, jenisBBM: String,
        depan:Double, tengah:Double,
        kondisi:String, belakang:Double,
        heel:Double, trim:Double, listOfTank: MutableList<SoundingItems>
    ){
        val apiService = ApiConfig.getApiService()
        val listOfTankJsonArray = mutableListOf<String>()
        for (item in listOfTank) {
            val gson = Gson()
            val itemJson = gson.toJson(item)
            listOfTankJsonArray.add(itemJson)
        }
        val listOfTankJson = listOfTankJsonArray.joinToString(", ")
        val json = """
            {
              "pelabuhan": "$pelabuhan",
              "tanggal_sounding": "$tanggal",
              "waktu_sounding": "$waktu",
              "jenis_BBM": "$jenisBBM",
              "draft_depan": ${depan},
              "draft_tengah": ${tengah},
              "ship_condition": "$kondisi",
              "draft_belakang": ${belakang},
              "heel_correction": ${heel},
              "trim": ${trim},
              "sounding": [$listOfTankJson]
            }
        """.trimIndent()

        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val client = apiService.rob("Bearer $token",body )
        client.enqueue(object: Callback<RobResponse> {
            override fun onResponse(call: Call<RobResponse>, response: Response<RobResponse>) {
                val responseBody = response.body()
                Log.e("request",json)
                if(response.isSuccessful){
                    if(responseBody != null ){
                        _data.value = response.body()
                        Log.e("RESULT",response.body().toString())
                    }
                }else{
                    Log.e("error request rob",response.errorBody().toString())
                }
            }
            override fun onFailure(call: Call<RobResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
            }

        })
    }

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }
}