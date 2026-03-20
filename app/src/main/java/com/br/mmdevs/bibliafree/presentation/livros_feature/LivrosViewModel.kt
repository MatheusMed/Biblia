package com.br.mmdevs.bibliafree.presentation.livros_feature

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.br.mmdevs.bibliafree.data.datastore.SettingsRepository
import com.br.mmdevs.bibliafree.data.service.DailyBibleWorker
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
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class LivrosViewModel @Inject constructor(
    private val manager: BibliaManager,
    private val settingsRepository: SettingsRepository,
    application: Application

) : ViewModel() {

    private val context = application.applicationContext

    private val _stateLivros: MutableState<LivroState> = mutableStateOf(LivroState.Loading)
    private val _searchQuery = mutableStateOf("")
    val searchQuery: MutableState<String> = _searchQuery


    val stateLivros: LivroState
        get() {
            val state = _stateLivros.value
            if (state is LivroState.Success && _searchQuery.value.isNotEmpty()) {
                val filtered = state.livros.filter { item ->
                    // Aqui usamos sua função getNomeLivro para filtrar pelo nome real
                    val nomeCompleto = getNomeLivro(item.abbrev)
                    nomeCompleto.contains(_searchQuery.value, ignoreCase = true)
                }
                return LivroState.Success(filtered)
            }
            return state
        }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private val _chapters = MutableStateFlow<List<List<String>>>(emptyList())
    val chapters: StateFlow<List<List<String>>> = _chapters


    val fontSize = settingsRepository.fontSizeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        16f
    )

    private val _showToast = MutableStateFlow(false)
    val showToast = _showToast


    val highlightedVerses = settingsRepository.highlightedVersesFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap()
    )

    fun toggleHighlight(verseId: String, color: Color) {
        viewModelScope.launch {
            val hex = if (color == Color.Transparent) "none" else String.format("#%06X", (0xFFFFFF and color.toArgb()))
            settingsRepository.saveHighlight(verseId, hex)
        }
    }
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
        scheduleDailyNotification(context)
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




    fun scheduleDailyNotification(context: Context) {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val initialDelay = dueDate.timeInMillis - currentDate.timeInMillis

        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyBibleWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED).build())
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "DailyVerseWork",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )
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


