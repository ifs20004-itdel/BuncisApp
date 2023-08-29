package com.example.buncisapp.views.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.buncisapp.R
import com.example.buncisapp.data.ShipModel
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.databinding.ActivityLoginBinding
import com.example.buncisapp.network.ApiConfig
import com.example.buncisapp.response.LoginResponse
import com.example.buncisapp.views.ViewModelFactory
import com.example.buncisapp.views.inputData.InputDataActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding : ActivityLoginBinding

    companion object{
        const val SHARED_PREFERENCES="shared_preferences"
        const val TOKEN = "token"
        const val ISLOGGEDIN = "isloggedin"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login -> {
                login()
            }
        }
    }

    private fun login() {
        val username= binding.edLoginUsername.text.toString().trim()
        val password = binding.edLoginPassword.text.toString().trim()
        ApiConfig
            .getApiService()
            .login(username, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.apply {
                            validateLogin(data.toString())
                        }
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT)
                            .show()
                        val mainIntent = Intent(this@LoginActivity, InputDataActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Data Invalid", Toast.LENGTH_SHORT).show()
                }
            })
    }



    private fun validateLogin(token: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(TOKEN, token)
        editor.putBoolean(ISLOGGEDIN, true)
        editor.apply()
    }
}