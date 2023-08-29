package com.example.buncisapp.views.inputData

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.buncisapp.data.ShipModel
import com.example.buncisapp.data.ShipPreference

class InputDataViewModel(private val pref: ShipPreference) : ViewModel(){
    fun getShip(): LiveData<ShipModel> {
        return pref.getShip().asLiveData()
    }
}