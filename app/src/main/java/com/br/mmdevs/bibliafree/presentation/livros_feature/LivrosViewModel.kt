package com.br.mmdevs.bibliafree.presentation.livros_feature

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.mmdevs.bibliafree.data.datastore.SettingsRepository
import com.br.mmdevs.bibliafree.domain.model.BookDto
import com.br.mmdevs.bibliafree.domain.usecase.BibliaManager
import com.br.mmdevs.bibliafree.presentation.livros_feature.states.LivroState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LivrosViewModel @Inject constructor(
    private val manager: BibliaManager,
    private val settingsRepository: SettingsRepository

) : ViewModel() {

    private val _stateLivros: MutableState<LivroState> = mutableStateOf(LivroState.Loading)
    var stateLivros: MutableState<LivroState> = _stateLivros

    private val _chapters = MutableStateFlow<List<List<String>>>(emptyList())
    val chapters: StateFlow<List<List<String>>> = _chapters


    val fontSize = settingsRepository.fontSizeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        16f
    )

    private val _showToast = MutableStateFlow(false)
    val showToast = _showToast

    fun increaseFont() {
        viewModelScope.launch {
            val newSize = (fontSize.value + 1f).coerceAtMost(30f)
            settingsRepository.saveFontSize(newSize)
            _showToast.value = !_showToast.value
        }
    }
    fun decresentFont() {
        viewModelScope.launch {
            val newSize = (fontSize.value - 1f).coerceAtMost(18f)
            settingsRepository.saveFontSize(newSize)
            _showToast.value = !_showToast.value
        }
    }



    init {
        fetchLivros()
    }




    fun getChaptersByAbbrev(abbrev: String) {
        viewModelScope.launch {
            val livros = withContext(Dispatchers.IO) {
                manager.getAllLivrosFromJson()
            }

            val livro = livros.find {
                it.abbrev?.trim().equals(abbrev.trim(), ignoreCase = true)
            }

            _chapters.value = livro?.chapters ?: emptyList()
        }
    }


    private fun fetchLivros() {
        viewModelScope.launch {
            _stateLivros.value = LivroState.Loading

            try {
                val livros = withContext(Dispatchers.IO) {
                    manager.getAllLivrosFromJson()
                }

                _stateLivros.value = LivroState.Success(livros)

            } catch (e: Exception) {
                _stateLivros.value =
                    LivroState.Error(e.message ?: "Erro ao trazer os livros")
            }
        }
    }
}


