package com.br.mmdevs.bibliafree.presentation.livros_feature


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.br.mmdevs.bibliafree.R
import com.br.mmdevs.bibliafree.utils.shareText
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands



@SuppressLint("ServiceCast")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VersiculoScreen(
    chapters: List<List<String>>,
    navControler: NavController,
    abbrev: String,
    fontSize: Float,
    increaseFont: () -> Unit,
    decreaseFont: () -> Unit,
    viewModel: LivrosViewModel

) {


    val showToast by viewModel.showToast.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var selectedVerses by remember { mutableStateOf(setOf<String>()) }

    val selectedTexts = remember { mutableStateMapOf<String, String>() }

    var expandedVerseId by remember { mutableStateOf<String?>(null) }
    var selectedText by remember { mutableStateOf("") }

        Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(top = 16.dp)
            )
        },
        topBar = {
            TopAppBar(
                title = {
                        Text(getNomeLivro(abbrev))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (selectedVerses.isNotEmpty()) {
                                selectedVerses = emptySet()
                                selectedTexts.clear()
                            } else {
                                navControler.popBackStack()
                            }
                        }
                    ) {
                        Icon(
                            painterResource(com.composables.icons.heroicons.mini.R.drawable.heroicons_ic_chevron_left_mini),
                            ""
                        )
                    }
                },

                actions = {
                    if (selectedVerses.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                val texto = selectedTexts.values.joinToString("\n\n")
                                shareText(context, texto)
                            }
                        ) {
                            Icon(
                                painterResource(com.composables.icons.heroicons.mini.R.drawable.heroicons_ic_share_mini),
                                "Compartilhar"
                            )
                        }
                    }

                    IconButton(onClick = { increaseFont() }) {
                        Icon(
                            painterResource(com.composables.icons.heroicons.micro.R.drawable.heroicons_ic_plus_micro),
                            ""
                        )
                    }

                    IconButton(onClick = { decreaseFont() }) {
                        Icon(
                            painterResource(com.composables.icons.heroicons.micro.R.drawable.heroicons_ic_minus_micro),
                            ""
                        )
                    }

                }
            )
        }
    ) {
        pading ->
        LazyColumn(
            modifier = Modifier.padding(pading)
        ) {
            itemsIndexed(chapters) { indexCapitulo, capitulo ->


                Text(
                    text = "Capítulo ${indexCapitulo + 1}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
                HorizontalDivider()


                capitulo.forEachIndexed { indexVerso, versiculo ->


                    val verseId = "${indexCapitulo}_${indexVerso}"

                    val isMenuOpen = expandedVerseId == verseId
                    val isSelected = selectedVerses.contains(verseId)

                    val textoCompleto =
                        "${getNomeLivro(abbrev)} ${indexCapitulo + 1}:${indexVerso + 1} - $versiculo"

                    Text(
                        text = "${indexVerso + 1}. $versiculo",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = fontSize.sp,
                            lineHeight = (fontSize + 8).sp
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp,
                                vertical = 6.dp)
                            .background(
                            if (isSelected) Color.LightGray.copy(alpha = 0.5f) else Color.Transparent
                                )
                            .clickable{
                                selectedText = textoCompleto
                                expandedVerseId = verseId
                            }
                    )

                    DropdownMenu(
                        expanded = isMenuOpen,
                        onDismissRequest = { expandedVerseId = null }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Compartilhar") },
                            onClick = {
                                shareText(context, selectedText)
                                expandedVerseId = null
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Copiar") },
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("versiculo", selectedText)
                                clipboard.setPrimaryClip(clip)
                                expandedVerseId = null
                            }
                        )
                    }



                }
            }
        }



    }


    ToastMessage(showToast, fontSize,snackbarHostState)
}

@Composable
fun ToastMessage(show: Boolean, fontSize: Float,snackbarHostState: SnackbarHostState) {

    val context = LocalContext.current

    LaunchedEffect(show) {
            if (show) {
                snackbarHostState.showSnackbar(
                    message = "Tamanho da Fonte: ${fontSize.toInt()}"
                )
            }


    }
}

