package com.example.bukuku.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Ekstensi Property untuk menginisialisasi DataStore Preferences pada Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_preferences")

/**
 * Pengelola Preferences DataStore untuk menyimpan pengaturan aplikasi,
 * seperti preferensi mode tampilan ringkas.
 */
class PreferencesManager(private val context: Context) {

    companion object {
        private val IS_COMPACT_VIEW_KEY = booleanPreferencesKey("is_compact_view")
    }

    /**
     * Flow untuk membaca status mode tampilan ringkas dari DataStore.
     */
    val isCompactViewFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_COMPACT_VIEW_KEY] ?: false // Default false (tampilan normal)
        }

    /**
     * Menyimpan status mode tampilan ringkas ke DataStore.
     */
    suspend fun setCompactView(isCompact: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_COMPACT_VIEW_KEY] = isCompact
        }
    }
}
