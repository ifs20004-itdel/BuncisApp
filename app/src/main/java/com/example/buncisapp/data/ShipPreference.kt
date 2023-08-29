package com.example.buncisapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShipPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getShip(): Flow<ShipModel> {
        return dataStore.data.map { preferences ->
            ShipModel(
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
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
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
        }
        dataStore.edit { preferences ->
            preferences [TOKEN_KEY] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ShipPreference? = null

        private val STATE_KEY = booleanPreferencesKey("state")
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