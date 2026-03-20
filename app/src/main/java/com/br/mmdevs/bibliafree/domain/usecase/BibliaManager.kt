package com.br.mmdevs.bibliafree.domain.usecase

import com.br.mmdevs.bibliafree.data.entity.LivroEntity
import com.br.mmdevs.bibliafree.domain.model.BookDto
import com.br.mmdevs.bibliafree.domain.model.HarpaItem
import com.br.mmdevs.bibliafree.domain.repository.LivroJsonRepository
import javax.inject.Inject

class BibliaManager @Inject constructor(
    private val jsonLivro: LivroJsonRepository
) {


     fun getAllLivrosFromJson():List<BookDto>{
        return jsonLivro.getLivros()
    }
    fun getHinos():List<HarpaItem>{
        return jsonLivro.getHinosHarpa()
    }

}