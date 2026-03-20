package com.br.mmdevs.bibliafree.presentation.livros_feature


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import compose.icons.tablericons.ClearAll
import compose.icons.tablericons.ColorPicker
import compose.icons.tablericons.Direction
import compose.icons.tablericons.Directions
import compose.icons.tablericons.DotsVertical
import compose.icons.tablericons.List
import compose.icons.tablericons.Note
import compose.icons.tablericons.Notebook
import compose.icons.tablericons.Palette
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
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val highlightedVerses by viewModel.highlightedVerses.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    var selectedVerses by remember { mutableStateOf(setOf<Pair<String, String>>()) }
    val isSelectionMode = selectedVerses.isNotEmpty()

    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = corLogo
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "${selectedVerses.size} versículo(s) selecionado(s)",
                    style = MaterialTheme.typography.titleMedium,
                    color = corPreto
                )

                Row(
                    modifier = Modifier.padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val colors = listOf(
                        Color(0xFFFFEB3B), // Amarelo
                        Color(0xFF81D4FA), // Azul
                        Color(0xFFA5D6A7), // Verde
                        Color(0xFFF48FB1), // Rosa
                        Color.Transparent  // Limpar
                    )

                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(
                                    if (color == Color.Transparent) Color.White else color,
                                    androidx.compose.foundation.shape.CircleShape
                                )
                                .border(
                                    1.dp,
                                    if (color == Color.Transparent) Color.LightGray else Color.Transparent,
                                    androidx.compose.foundation.shape.CircleShape
                                )
                                .clickable {
                                    // Aplica a cor para todos os selecionados
                                    selectedVerses.forEach { (id, _) ->
                                        viewModel.toggleHighlight(id, color)
                                    }
                                    selectedVerses = emptySet()
                                    showSheet = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (color == Color.Transparent) {
                                Icon(TablerIcons.ClearAll, null, tint = Color.Gray)
                            }
                        }
                    }
                }

                TextButton(
                    onClick = {
                        val textToShare = selectedVerses
                            .sortedBy { it.first }
                            .joinToString("\n\n") { it.second }

                        shareText(context, textToShare)
                        selectedVerses = emptySet()
                        showSheet = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = corLogo),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(FeatherIcons.Share, null, modifier = Modifier.padding(end = 8.dp))
                    Text("Compartilhar Versiculos Selecionados")
                }

                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    Scaffold(
        containerColor = corLogo,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = corLogo,
                    titleContentColor = corPreto,
                    navigationIconContentColor = corPreto,
                ),
                title = {
                    Text(if (isSelectionMode) "${selectedVerses.size} selecionados" else getNomeLivro(abbrev))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isSelectionMode) selectedVerses = emptySet()
                        else navControler.popBackStack()
                    }) {
                        Icon(FeatherIcons.ArrowLeft, "", tint = corPreto)
                    }
                },
                actions = {
                    if (isSelectionMode) {

                        IconButton(onClick = { showSheet = true }) {
                            Icon(TablerIcons.ColorPicker,
                                "Colorir",
                                tint = corPreto)
                        }
                    } else {

                        var mainExpansion by remember { mutableStateOf(false) }
                        var chapterExpansion by remember { mutableStateOf(false) }

                        Box {
                            IconButton(onClick = { mainExpansion = true }) {
                                Icon(TablerIcons.DotsVertical, "Opções", tint = corPreto)
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
                                        chapterExpansion = true
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Aumentar Letra", color = corPreto) },
                                    leadingIcon = { Icon(FeatherIcons.Plus, null, tint = corPreto) },
                                    onClick = { increaseFont() }
                                )
                                DropdownMenuItem(
                                    text = { Text("Diminuir Letra", color = corPreto) },
                                    leadingIcon = { Icon(FeatherIcons.Minus, null, tint = corPreto) },
                                    onClick = { decreaseFont() }
                                )
                            }

                            DropdownMenu(
                                containerColor = corLogo,
                                expanded = chapterExpansion,
                                onDismissRequest = { chapterExpansion = false }
                            ) {
                                chapters.forEachIndexed { index, _ ->
                                    DropdownMenuItem(
                                        colors = MenuDefaults.itemColors(textColor = corPreto),
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
                    }
                }
            )
        }
    ) { padding ->
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
                    val verseId = "${abbrev}_${indexCapitulo}_${indexVerso}"

                    // Verifica se está selecionado
                    val isSelected = selectedVerses.any { it.first == verseId }

                    // Cor de fundo (Prioridade: Seleção > Destaque Salvo)
                    val hexColor = highlightedVerses[verseId]
                    val savedColor = if (hexColor != null && hexColor != "none") {
                        Color(android.graphics.Color.parseColor(hexColor))
                    } else {
                        Color.Transparent
                    }

                    val backgroundColor = if (isSelected) corAzul.copy(alpha = 0.3f) else savedColor

                    Text(
                        text = "${indexVerso + 1}. $versiculo",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = fontSize.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = (fontSize + 8).sp,
                            color = if (isSelected) corAzul else corPreto
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backgroundColor)
                            .clickable {
                                val currentPair = verseId to "${indexVerso + 1}. $versiculo"
                                if (isSelected) {
                                    selectedVerses = selectedVerses - currentPair
                                } else {
                                    selectedVerses = selectedVerses + currentPair
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}