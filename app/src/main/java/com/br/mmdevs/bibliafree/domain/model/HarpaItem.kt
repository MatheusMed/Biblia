package com.br.mmdevs.bibliafree.domain.model

data class HarpaItem(
    val hino: String,
    val coro: String?,
    val verses: Map<String, String> // Os versos vêm como "1": "texto", "2": "texto"
)