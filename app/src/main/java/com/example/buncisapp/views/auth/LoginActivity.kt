package com.example.buncisapp.views.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
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

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ShipPreference.getInstance(dataStore),this)
        )[LoginViewModel::class.java]

        loginViewModel.getShip().observe(this, { user ->
            if(user.isLogin){
                startActivity(Intent(this@LoginActivity, InputDataActivity::class.java))
            }
        })
    }

    private fun setupAction(){
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginUsername.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()
            ApiConfig
                .getApiService()
                .login(email, password)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            loginViewModel.login()
                            val ship = response.body() as ShipModel
                            loginViewModel.saveShip(ShipModel(ship.token,true))
                            Toast.makeText(this@LoginActivity,"success", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Data Invalid", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}