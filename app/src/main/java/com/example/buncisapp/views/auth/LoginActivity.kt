package com.example.buncisapp.views.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.databinding.ActivityLoginBinding
import com.example.buncisapp.utils.AuthenticationCallback
import com.example.buncisapp.views.ViewModelFactory
import com.example.buncisapp.views.inputData.InputDataActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity(), AuthenticationCallback{

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

        loginViewModel.getShip().observe(this) { user ->
            if(user.isLogin == 1){
                val intent = Intent(this@LoginActivity, InputDataActivity::class.java)
                intent.putExtra("username", USERNAME)
                startActivity(intent)
            }
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            USERNAME = binding.edLoginUsername.text.toString()
            val bunkerCode = USERNAME
            val password = binding.edLoginPassword.text.toString()
            if (bunkerCode.isEmpty() || password.isEmpty()){
                Toast.makeText(this@LoginActivity, "Isi data yang kosong!", Toast.LENGTH_SHORT).show()
            }else{
                loginViewModel.validateShip(bunkerCode,password, this)
            }
        }
    }
    override fun onError(isLogin: Int?, message: String?) {
        Toast.makeText(this@LoginActivity, message.toString(), Toast.LENGTH_SHORT).show()
    }
    companion object{
        private var USERNAME = "user"
    }
}