package com.br.mmdevs.bibliafree.presentation.harpa_feature

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.mmdevs.bibliafree.domain.model.HarpaItem
import com.br.mmdevs.bibliafree.domain.usecase.BibliaManager
import com.br.mmdevs.bibliafree.presentation.harpa_feature.state.HarpaState
import com.br.mmdevs.bibliafree.presentation.livros_feature.states.LivroState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HarpaViewModel @Inject constructor(
    private val manager: BibliaManager,
): ViewModel() {

    private val _stateHinos = mutableStateOf<HarpaState>(HarpaState.Loading)



    init {
        feathHinos()
    }


    fun getHinoById(id: String): HarpaItem? {
        val currentState = _stateHinos.value
        return if (currentState is HarpaState.Success) {

            currentState.hinos.find { it.hino.startsWith(id) }
        } else null
    }

    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery


    val stateHinos: HarpaState
        get() {
            val state = _stateHinos.value
            if (state is HarpaState.Success && _searchQuery.value.isNotEmpty()) {
                val filteredList = state.hinos.filter { hino ->

                    hino.hino.contains(_searchQuery.value, ignoreCase = true)
                }
                return HarpaState.Success(filteredList)
            }
            return state
        }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private fun feathHinos() {
        viewModelScope.launch {
            _stateHinos.value = HarpaState.Loading

            try {
                val hinos = withContext(Dispatchers.IO) {
                    manager.getHinos()
                }

                _stateHinos.value = HarpaState.Success(hinos)

            } catch (e: Exception) {
                _stateHinos.value =
                    HarpaState.Error(e.message ?: "Erro ao trazer os livros")
            }
        }
    }
}