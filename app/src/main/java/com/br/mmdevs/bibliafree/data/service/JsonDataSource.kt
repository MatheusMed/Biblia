package com.br.mmdevs.bibliafree.data.service

import android.content.Context
import com.br.mmdevs.bibliafree.domain.model.BookDto
import com.google.gson.Gson
import javax.inject.Inject

class JsonDataSource @Inject constructor(
    private val context: Context
) {

    fun loadBooks(): List<BookDto> {
        return try {
            val json = context.assets.open("nvi.json")
                .bufferedReader()
                .use { it.readText() }
                .replace("\uFEFF", "")

            println("JSON LIMPO: ${json.take(100)}")

            Gson().fromJson(
                json,
                Array<BookDto>::class.java
            ).toList()

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}