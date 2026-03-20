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
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.br.mmdevs.bibliafree.presentation.ui.theme.corAzul
import com.br.mmdevs.bibliafree.presentation.ui.theme.corLogo
import com.br.mmdevs.bibliafree.presentation.ui.theme.corPreto
import com.br.mmdevs.bibliafree.utils.shareText
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.TablerIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Minus
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Share
import compose.icons.fontawesomeicons.Brands
import compose.icons.tablericons.Direction
import compose.icons.tablericons.Directions
import compose.icons.tablericons.DotsVertical
import compose.icons.tablericons.List
import compose.icons.tablericons.Note
import compose.icons.tablericons.Notebook
import kotlinx.coroutines.launch


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
    // 1. Estado do Scroll e Scope para animação
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 2. Estado para o Dropdown
    var menuExpanded by remember { mutableStateOf(false) }

    val showToast by viewModel.showToast.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var selectedVerses by remember { mutableStateOf(setOf<String>()) }

    val selectedTexts = remember { mutableStateMapOf<String, String>() }

    var expandedVerseId by remember { mutableStateOf<String?>(null) }
    var selectedText by remember { mutableStateOf("") }

        Scaffold(
            containerColor = corLogo,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(top = 16.dp)
            )
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = corLogo,
                    titleContentColor = corPreto,
                    navigationIconContentColor = corPreto,
                ),

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
                            FeatherIcons.ArrowLeft,
                            "",
                            tint = corPreto,
                        )
                    }
                },

                actions = {

                        var mainExpansion by remember { mutableStateOf(false) }
                        var chapterExpansion by remember { mutableStateOf(false) }

                        Box {
                            IconButton(onClick = { mainExpansion = true }) {

                                Icon(TablerIcons.DotsVertical, contentDescription = "Opções", tint = corPreto)
                            }

                            DropdownMenu(
                                containerColor = corLogo,
                                expanded = mainExpansion,
                                onDismissRequest = { mainExpansion = false }
                            ) {

                                DropdownMenuItem(
                                    text = { Text("Ir para Capítulo...", color = corPreto) },
                                    leadingIcon = { Icon(TablerIcons.Directions, null, tint = corPreto) },
                                    onClick = {
                                        mainExpansion = false
                                        chapterExpansion = true // Abre o segundo menu de capítulos
                                    }
                                )

                                HorizontalDivider()


                                DropdownMenuItem(
                                    text = { Text("Aumentar Letra",color = corPreto) },
                                    leadingIcon = { Icon(FeatherIcons.Plus, null, tint = corPreto) },
                                    onClick = { increaseFont() }
                                )


                                DropdownMenuItem(
                                    text = { Text("Diminuir Letra", color = corPreto) },
                                    leadingIcon = { Icon(FeatherIcons.Minus, null,tint = corPreto) },
                                    onClick = { decreaseFont() }
                                )


                                if (selectedVerses.isNotEmpty()) {
                                    HorizontalDivider()
                                    DropdownMenuItem(
                                        text = { Text("Compartilhar Seleção") },
                                        leadingIcon = { Icon(FeatherIcons.Share, null) },
                                        onClick = {
                                            mainExpansion = false
                                            val texto = selectedTexts.values.joinToString("\n\n")
                                            shareText(context, texto)
                                        }
                                    )
                                }
                            }


                            DropdownMenu(
                                containerColor = corLogo,
                                expanded = chapterExpansion,
                                onDismissRequest = { chapterExpansion = false }
                            ) {
                                chapters.forEachIndexed { index, _ ->
                                    DropdownMenuItem(
                                        colors = MenuDefaults.itemColors(
                                            textColor = corPreto,          // Cor padrão do texto
                                            leadingIconColor = corAzul,     // Cor padrão do ícone
                                            trailingIconColor = corAzul,    // Cor do ícone da direita (se houver)

                                        ),

                                        text = { Text("Capítulo ${index + 1}") },
                                        onClick = {
                                            chapterExpansion = false
                                            coroutineScope.launch {
                                                listState.scrollToItem(index)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    })

        }
    ) {
                padding ->
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(padding)
            ) {
                itemsIndexed(chapters) { indexCapitulo, capitulo ->
                    Text(
                        text = "Capítulo ${indexCapitulo + 1}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(8.dp),
                        color = corAzul
                    )
                    capitulo.forEachIndexed { indexVerso, versiculo ->
                        Text(
                            text = "${indexVerso + 1}. $versiculo",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = fontSize.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = (fontSize + 8).sp
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    ToastMessage(showToast, fontSize, snackbarHostState)
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

