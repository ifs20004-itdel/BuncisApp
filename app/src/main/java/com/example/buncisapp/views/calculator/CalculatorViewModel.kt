package com.example.buncisapp.views.calculator

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.ShipModel
import com.example.buncisapp.data.model.SoundingItems
import com.example.buncisapp.data.response.BunkerResponse
import com.example.buncisapp.data.response.CalculationResponse
import com.example.buncisapp.data.response.FuelTankResponse
import com.example.buncisapp.data.retrofit.ApiConfig
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalculatorViewModel(private val pref: ShipPreference): ViewModel() {

    private val _noTanki = MutableLiveData<List<String?>>()
    val noTanki: LiveData<List<String?>> = _noTanki

    private val _data = MutableLiveData<BunkerResponse>()
    val data : LiveData<BunkerResponse> = _data

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
        level_sounding: Double
    ){
        val apiService = ApiConfig.getApiService()
        val json = """
            {
              "heel_correction": 0.0,
              "trim": ${trim},
              "nomor_tanki": "${nomor_tanki}",
              "level_sounding":${level_sounding}
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        Log.e("Ini cuyy",json)
        val client = apiService.calculation("Bearer $token",body )
        client.enqueue(object: Callback<CalculationResponse> {
            override fun onResponse(call: Call<CalculationResponse>, response: Response<CalculationResponse>) {
                val responseBody = response.body()
                Log.e("iniii",response.errorBody().toString())
                if(response.isSuccessful){
                    if(responseBody != null ){
                        _calculation.value = response.body()
                            Log.e("berhasil",response.body().toString())
                    }
                }else{
                    Log.e("iniii",response.errorBody().toString())
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
        waktu: String, bbm: String,
        depan:Double, tengah:Double,
        kondisi:String, belakang:Double,
        heel:Int, trim:Double, listOfTank: MutableSet<SoundingItems>
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
              "pelabuhan": "Ketapang",
              "tanggal_sounding": "2023-08-13",
              "waktu_sounding": "15:01:02",
              "jenis_BBM": "Pertamax Racing",
              "draft_depan": 3,
              "draft_tengah": 3,
              "ship_condition": "REDELIVERY",
              "draft_belakang": 3,
              "heel_correction": 0,
              "trim": -2.5,
              "sounding": [$listOfTankJson]
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        Log.e("Ini cuyy",json)
        val client = apiService.getBunker("Bearer $token",body )
        client.enqueue(object: Callback<BunkerResponse> {
            override fun onResponse(call: Call<BunkerResponse>, response: Response<BunkerResponse>) {
                val responseBody = response.body()
                Log.e("iniii",response.errorBody().toString())
                if(response.isSuccessful){
                    if(responseBody != null ){
                        _data.value = response.body()
                    }
                }else{
                    Log.e("iniii",response.errorBody().toString())
                }
            }
            override fun onFailure(call: Call<BunkerResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
            }

        })
    }
}