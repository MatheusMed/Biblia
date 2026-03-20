package com.br.mmdevs.bibliafree.presentation.harpa_feature.state

import com.br.mmdevs.bibliafree.domain.model.BookDto
import com.br.mmdevs.bibliafree.domain.model.HarpaItem
import com.br.mmdevs.bibliafree.presentation.livros_feature.states.LivroState

sealed class HarpaState {
    object Loading:HarpaState()
    data class Success(val hinos:List<HarpaItem>):HarpaState()
    data class Error(val message:String):HarpaState()
}