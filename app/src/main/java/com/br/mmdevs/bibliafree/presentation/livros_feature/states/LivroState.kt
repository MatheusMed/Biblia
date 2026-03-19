package com.br.mmdevs.bibliafree.presentation.livros_feature.states

import com.br.mmdevs.bibliafree.data.entity.LivroEntity
import com.br.mmdevs.bibliafree.domain.model.BookDto

sealed class LivroState {
     object Loading:LivroState()
     data class Success(val livros:List<BookDto>):LivroState()
     data class Error(val message:String):LivroState()
}