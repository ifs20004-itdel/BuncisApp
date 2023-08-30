package com.example.buncisapp.views.auth

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.ShipModel
import com.example.buncisapp.data.response.LoginResponse
import com.example.buncisapp.data.retrofit.ApiConfig
import com.example.buncisapp.utils.AuthenticationCallback
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: ShipPreference) : ViewModel() {
    fun getShip(): LiveData<ShipModel> {
        return pref.getShip().asLiveData()
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }

    fun saveShip(ship: ShipModel){
        viewModelScope.launch {
            pref.saveShip(ship)
        }
    }
    fun validateShip(bunkerCode: String, password: String, stateCallback: AuthenticationCallback){
        val apiService = ApiConfig.getApiService()
        val json = """
            {
            "bunker_code": "$bunkerCode",
            "password": "$password"
            }
        """.trimIndent()

        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val login = apiService.login(body)
        login.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                if(response.isSuccessful){
                    if(responseBody != null && responseBody.status == 1 ){
                        login()
                        if(responseBody.data != null){
                            saveShip(ShipModel(responseBody.data, responseBody.status))
                        }
                    }
                }else{
                    val errorMessage = "Username atau password salah"
                    stateCallback.onError(-1, errorMessage)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
                stateCallback.onError(-1, t.message.toString())
            }

        })
    }
}