package com.br.mmdevs.bibliafree.data.service

import android.content.Context
import com.br.mmdevs.bibliafree.domain.model.BookDto
import com.br.mmdevs.bibliafree.domain.model.HarpaItem
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

            Gson().fromJson(
                json,
                Array<BookDto>::class.java
            ).toList()

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    fun loadHarpa(): List<HarpaItem> {
        return try {
            val jsonString = context.assets.open("harpa.json") // Certifique-se do nome do arquivo
                .bufferedReader()
                .use { it.readText() }
//                .replace("\uFEFF", "")

            // Como o JSON começa com "25": { ... }, ele é um Map (Dicionário)
            val type = object : com.google.gson.reflect.TypeToken<Map<String, HarpaItem>>() {}.type
            val harpaMap: Map<String, HarpaItem> = Gson().fromJson(jsonString, type)

            println("JSON LIMPO: ${harpaMap.size}")
            harpaMap.values.map { hino ->
                hino.copy(
                    // Limpa o <br> do coro se existir
                    coro = hino.coro?.replace("<br>", "\n")?.trim(),
                    // Limpa o <br> de cada verso
                    verses = hino.verses.mapValues { entry ->
                        entry.value.replace("<br>", "\n").trim()
                    }
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


}