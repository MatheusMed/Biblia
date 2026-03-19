package com.br.mmdevs.bibliafree.domain.repository

import com.br.mmdevs.bibliafree.data.service.JsonDataSource
import com.br.mmdevs.bibliafree.domain.model.BookDto
import javax.inject.Inject

class LivroJsonRepository @Inject  constructor(
    private val dataSource: JsonDataSource
) {
    fun getLivros(): List<BookDto> {
        return dataSource.loadBooks()
    }
}