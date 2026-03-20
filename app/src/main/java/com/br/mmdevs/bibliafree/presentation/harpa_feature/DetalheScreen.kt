package com.br.mmdevs.bibliafree.presentation.harpa_feature

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br.mmdevs.bibliafree.domain.model.HarpaItem
import com.br.mmdevs.bibliafree.presentation.ui.theme.corBranco
import com.br.mmdevs.bibliafree.presentation.ui.theme.corLogo
import com.br.mmdevs.bibliafree.presentation.ui.theme.corPreto
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Minus
import compose.icons.feathericons.Plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheHinoScreen(
    hino: HarpaItem?,
    onBack: () -> Unit
) {

    var fontSize by remember { mutableFloatStateOf(18f) }

    Scaffold(
        containerColor = corLogo,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = hino?.hino?.split(" - ")?.lastOrNull() ?: "Hino",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(FeatherIcons.ArrowLeft, contentDescription = "Voltar")
                    }
                },
                actions = {

                    IconButton(onClick = { if (fontSize < 35f) fontSize += 2f }) {
                        Icon(FeatherIcons.Plus, "Aumentar fonte", tint = corPreto)
                    }
                    IconButton(onClick = { if (fontSize > 12f) fontSize -= 2f }) {
                        Icon(FeatherIcons.Minus, "Diminuir fonte", tint = corPreto)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = corLogo,
                    titleContentColor = corPreto,
                    navigationIconContentColor = corPreto,
                ),
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            hino?.let { item ->

                Text(
                    text = item.hino,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = (fontSize + 4).sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))


                val listaVersos = item.verses.entries.toList()
                val totalVersos = listaVersos.size
                val pontoDeInsercao = if (totalVersos > 1) totalVersos / 2 else 1


                listaVersos.take(pontoDeInsercao).forEach { (num, texto) ->
                    VersoItem(num, texto, fontSize)
                }


                if (!item.coro.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = corBranco.copy(alpha = 0.6f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "CORO",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.coro,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = fontSize.sp,
                                    lineHeight = (fontSize * 1.4).sp,
                                    fontStyle = FontStyle.Italic
                                ),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }


                listaVersos.drop(pontoDeInsercao).forEach { (num, texto) ->
                    VersoItem(num, texto, fontSize)
                }

                Spacer(modifier = Modifier.height(40.dp))

            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Hino não encontrado", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun VersoItem(numero: String, texto: String, fontSize: Float) {
    Row(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = "$numero.",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(32.dp)
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = fontSize.sp,
                lineHeight = (fontSize * 1.5).sp
            )
        )
    }
}