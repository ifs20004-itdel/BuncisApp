package com.example.buncisapp.views

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.views.auth.LoginViewModel
import com.example.buncisapp.views.inputData.InputDataViewModel

class ViewModelFactory(private val pref: ShipPreference, private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
//            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
//                MainMenuViewModel(pref, Injection.provideRepository(context)) as T
//            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(InputDataViewModel::class.java)->{
                InputDataViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}