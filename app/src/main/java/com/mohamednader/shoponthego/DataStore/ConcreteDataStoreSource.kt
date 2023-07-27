package com.mohamednader.shoponthego.DataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class ConcreteDataStoreSource(context: Context) : DataStoreSource{

    private val dataStore = context.dataStore


     override suspend fun saveStringDS(key: Preferences.Key<String>, value: String) {
        try {
            dataStore.edit { preferences ->
                preferences[key] = value
            }
        } catch (e: IOException) {
            // Handle exception if needed
        }
    }

    override fun getStringDS(key: Preferences.Key<String>): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                 emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[key]
            }
    }

}