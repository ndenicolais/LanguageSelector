package com.denicks21.languageselector.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepository(context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(name = "LanguageData")
    private val defaultLanguage = 1

    companion object {
        val PREF_LANGUAGE = preferencesKey<Int>("language")
        private var INSTANCE: DataStoreRepository? = null

        fun getInstance(context: Context): DataStoreRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }
                val instance = DataStoreRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun setLanguage(language: Int) {
        dataStore.edit { preferences ->
            preferences[PREF_LANGUAGE] = language
        }
    }

    val getLanguage: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[PREF_LANGUAGE] ?: defaultLanguage
        }
}