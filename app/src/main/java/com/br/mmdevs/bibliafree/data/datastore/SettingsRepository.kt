package com.br.mmdevs.bibliafree.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val context: Context
) {

    companion object {
        val FONT_SIZE_KEY = floatPreferencesKey("font_size")
        val HIGHLIGHTED_VERSES_KEY = stringPreferencesKey("highlighted_verses")
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




    val highlightedVersesFlow: Flow<Map<String, String>> = context.dataStore.data
        .map { prefs ->
            val data = prefs[HIGHLIGHTED_VERSES_KEY] ?: ""
            if (data.isEmpty()) emptyMap()
            else data.split(",").associate {
                val (key, value) = it.split("=")
                key to value
            }
        }

    suspend fun saveHighlight(verseId: String, colorHex: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[HIGHLIGHTED_VERSES_KEY] ?: ""
            val map = if (current.isEmpty()) mutableMapOf() else current.split(",").associate { it.split("=").let { it[0] to it[1] } }.toMutableMap()

            if (colorHex == "none") map.remove(verseId) else map[verseId] = colorHex

            prefs[HIGHLIGHTED_VERSES_KEY] = map.entries.joinToString(",") { "${it.key}=${it.value}" }
        }
    }
}