package com.example.buncisapp.views.calculator

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.Hasil
import com.example.buncisapp.data.model.Request
import com.example.buncisapp.data.model.ShipModel
import com.example.buncisapp.data.response.BunkerResponse
import com.example.buncisapp.data.response.FuelTankResponse
import com.example.buncisapp.data.response.FuelTypeResponse
import com.example.buncisapp.data.response.SoundingItem
import com.example.buncisapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalculatorViewModel(private val pref: ShipPreference): ViewModel() {

    private val _noTanki = MutableLiveData<List<String?>>()
    val noTanki: LiveData<List<String?>> = _noTanki

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
    fun postResult(
        token: String,
        pelabuhan: String, tanggal:String,
        waktu: String, bbm: String,
        depan:String, tengah:String,
        kondisi:String, belakang:String,
        heel:String, trim:String, listOfTank: MutableSet<SoundingItem>
    ){
        val apiService = ApiConfig.getApiService()
        val client = apiService.getBunker("Bearer $token",pelabuhan,tanggal,waktu,bbm, depan,tengah,kondisi,belakang,heel,trim,listOfTank )
        client.enqueue(object: Callback<BunkerResponse> {
            override fun onResponse(call: Call<BunkerResponse>, response: Response<BunkerResponse>) {
                val responseBody = response.body()
                if(response.isSuccessful){
                    if(responseBody != null ){
                        if(responseBody.sounding != null){
                            val data = Hasil(data.nama, data.tanggal, data.waktu,data.bahanBakar,data.depan,data.tengah,data.kondisiKapal,data.belakang,data.draft,listOfTank)
                        }
                    }
                }else{
                    val errorMessage = "Silahkan isi ulang data"
                }
            }

            override fun onFailure(call: Call<BunkerResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
            }

        })
    }
}