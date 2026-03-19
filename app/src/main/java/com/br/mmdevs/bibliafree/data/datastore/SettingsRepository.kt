package com.br.mmdevs.bibliafree.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val context: Context
) {

    companion object {
        val FONT_SIZE_KEY = floatPreferencesKey("font_size")
    }

    val fontSizeFlow: Flow<Float> = context.dataStore.data
        .map { prefs ->
            prefs[FONT_SIZE_KEY] ?: 16f
        }

    suspend fun saveFontSize(size: Float) {
        context.dataStore.edit { prefs ->
            prefs[FONT_SIZE_KEY] = size
        }
    }
}