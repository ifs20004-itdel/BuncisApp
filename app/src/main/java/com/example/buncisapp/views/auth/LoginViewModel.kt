package com.example.buncisapp.views.auth

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.ShipModel
import com.example.buncisapp.data.response.LoginResponse
import com.example.buncisapp.data.response.VesselResponse
import com.example.buncisapp.data.retrofit.ApiConfig
import com.example.buncisapp.utils.AuthenticationCallback
import com.example.buncisapp.utils.ExtractMessage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: ShipPreference) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getShip(): LiveData<ShipModel> {
        return pref.getShip().asLiveData()
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }

    fun saveShip(ship: ShipModel) {
        viewModelScope.launch {
            pref.saveShip(ship)
        }
    }

    fun validateShip(bunkerCode: String, password: String, stateCallback: AuthenticationCallback) {
        _loading.value = true
        val apiService = ApiConfig.getApiService()
        val json = """
            {
            "bunker_code": "$bunkerCode",
            "password": "$password"
            }
        """.trimIndent()

        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val login = apiService.login(body)
        login.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _loading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null && responseBody.status == 1) {
                        login()
                        if (responseBody.data != null) {
                            saveShip(ShipModel(responseBody.data, responseBody.status, bunkerCode))
                        }
                    }
                } else {
                    val errorResponse = response.errorBody()?.string().toString()
                    val message = ExtractMessage.extractMessage(errorResponse)
                    stateCallback.onError(-1, message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loading.value = false
                Log.e(ContentValues.TAG, "OnFailure: ${t.message.toString()}")
                stateCallback.onError(-1, t.message.toString())
            }
        })
    }

    fun getVessel(callback: (Boolean, List<String?>) -> Unit) {
        val apiService = ApiConfig.getApiService()
        val login = apiService.getVessel()
        login.enqueue(object : Callback<VesselResponse> {
            override fun onResponse(
                call: Call<VesselResponse>,
                response: Response<VesselResponse>
            ) {
                _loading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if (responseBody != null) {
                        responseBody.data?.let { callback(true, it.fuelTank) }
                    }
                }
            }

            override fun onFailure(call: Call<VesselResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "OnFailure: ${t.message.toString()}")
            }

        })
    }
}