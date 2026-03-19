package com.br.mmdevs.bibliafree.domain.usecase

import com.br.mmdevs.bibliafree.data.entity.LivroEntity
import com.br.mmdevs.bibliafree.domain.model.BookDto
import com.br.mmdevs.bibliafree.domain.repository.ILivroRepository
import com.br.mmdevs.bibliafree.domain.repository.LivroJsonRepository
import javax.inject.Inject

class BibliaManager @Inject constructor(
    private val livroRepository: ILivroRepository,
    private val jsonLivro: LivroJsonRepository
) {
    suspend fun getAllLivros():List<LivroEntity>{
        return livroRepository.getAllLivros()
    }

     fun getAllLivrosFromJson():List<BookDto>{
        return jsonLivro.getLivros()
    }

}