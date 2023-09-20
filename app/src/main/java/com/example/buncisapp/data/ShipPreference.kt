package com.example.buncisapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.buncisapp.data.model.ShipModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShipPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getShip(): Flow<ShipModel> {
        return dataStore.data.map { preferences ->
            ShipModel(
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: -1
            )
        }
    }

    suspend fun saveShip(ship: ShipModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ship.token
            preferences[STATE_KEY] = ship.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = 1
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = -1
        }
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ShipPreference? = null

        private val STATE_KEY = intPreferencesKey("state")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): ShipPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = ShipPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}