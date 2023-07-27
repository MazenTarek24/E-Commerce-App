package com.mohamednader.shoponthego.DataStore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreSource {

    suspend fun saveStringDS(key: Preferences.Key<String>, value: String)
    fun getStringDS(key: Preferences.Key<String>): Flow<String?>

}