package com.example.buncisapp.views.calculator

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.ShipModel
import com.example.buncisapp.data.response.BunkerResponse
import com.example.buncisapp.data.response.LoginResponse
import com.example.buncisapp.data.response.SoundingItem
import com.example.buncisapp.data.retrofit.ApiConfig
import com.example.buncisapp.utils.AuthenticationCallback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalculatorViewModel(private val pref: ShipPreference): ViewModel() {

    private val _noTanki = MutableLiveData<List<String?>>()
    val noTanki: LiveData<List<String?>> = _noTanki

    fun postResult(token: String, pelabuhan:String,tanggal: String,waktu: String,bbm: String, depan:String,tengah:String,kondisi:String,belakang:String,heel:String, trim:String,listOfTank : List<SoundingItem>, stateCallback: AuthenticationCallback){
        val apiService = ApiConfig.getApiService()
        val client = apiService.getBunker("Bearer $token",pelabuhan,tanggal,waktu,bbm, depan,tengah,kondisi,belakang,heel,trim,listOfTank )
        client.enqueue(object: Callback<BunkerResponse> {
            override fun onResponse(call: Call<BunkerResponse>, response: Response<BunkerResponse>) {
                val responseBody = response.body()
                if(response.isSuccessful){
                    if(responseBody != null ){
                        if(responseBody.sounding != null){
//                            saveShip(ShipModel(responseBody.data, responseBody.status))
                        }
                    }
                }else{
                    val errorMessage = "Silahkan isi ulang data"
                    stateCallback.onError(-1, errorMessage)
                }
            }

            override fun onFailure(call: Call<BunkerResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
                stateCallback.onError(-1, t.message.toString())
            }

        })
    }
}