package com.br.mmdevs.bibliafree.domain.repository

import com.br.mmdevs.bibliafree.data.service.JsonDataSource
import com.br.mmdevs.bibliafree.domain.model.BookDto
import com.br.mmdevs.bibliafree.domain.model.HarpaItem
import javax.inject.Inject

class LivroJsonRepository @Inject  constructor(
    private val dataSource: JsonDataSource
) {
    fun getLivros(): List<BookDto> {
        return dataSource.loadBooks()
    }
    fun getHinosHarpa():List<HarpaItem>{
        return dataSource.loadHarpa()
    }
}