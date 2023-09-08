package com.example.buncisapp.views.record

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.ShipModel
import com.example.buncisapp.data.model.SoundingItems
import com.example.buncisapp.data.response.RobResponse
import com.example.buncisapp.data.retrofit.ApiConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordViewModel(private val pref: ShipPreference): ViewModel()  {

    private val _rob = MutableLiveData<RobResponse>()
    val rob : LiveData<RobResponse> = _rob

    fun getShip(): LiveData<ShipModel> {
        return pref.getShip().asLiveData()
    }

    fun generate(
        token: String,
        pelabuhan: String,
        tanggal:String,
        waktu: String,
        jenisBBM: String,
        depan:Double,
        tengah:Double,
        kondisi: String,
        belakang:Double,
        heel:Double,
        trim: Double,
        listOfTank: MutableSet<SoundingItems>
    ){
        val apiService = ApiConfig.getApiService()
        val json = """
            {
              "pelabuhan": "${pelabuhan}",
              "tanggal_sounding": "${tanggal}",
              "waktu_sounding": "${waktu}",
              "jenis_BBM": "${jenisBBM}",
              "draft_depan": ${depan},
              "draft_tengah": ${tengah},
              "ship_condition": "${kondisi}",
              "draft_belakang": ${belakang},
              "heel_correction": ${heel},
              "trim": ${trim},
              "sounding": [$listOfTank]
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        Log.e("Ini cuyy",json)
        val client = apiService.rob("Bearer $token",body )
        client.enqueue(object: Callback<RobResponse> {
            override fun onResponse(call: Call<RobResponse>, response: Response<RobResponse>) {
                val responseBody = response.body()
                Log.e("iniii",response.errorBody().toString())
                if(response.isSuccessful){
                    if(responseBody != null ){
                        _rob.value = response.body()
                        Log.e("berhasil",response.body().toString())
                    }
                }else{
                    Log.e("iniii",response.errorBody().toString())
                }
            }
            override fun onFailure(call: Call<RobResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
            }

        })
    }
}