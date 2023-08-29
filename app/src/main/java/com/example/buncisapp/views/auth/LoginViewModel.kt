package com.example.buncisapp.views.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.buncisapp.data.ShipModel
import com.example.buncisapp.data.ShipPreference
import kotlinx.coroutines.launch

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
            pref.saveShip(ShipModel(ship.token,true))
        }
    }
}