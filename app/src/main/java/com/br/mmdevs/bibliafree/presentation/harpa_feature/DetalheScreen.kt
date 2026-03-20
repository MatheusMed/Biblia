package com.br.mmdevs.bibliafree.presentation.harpa_feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheHinoScreen(
    hino: HarpaItem?,
    onBack: () -> Unit
) {
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
                        Icon(
                            FeatherIcons.ArrowLeft,
                            contentDescription = "Voltar"
                        )
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
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))


                if (!item.coro.isNullOrEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = corBranco.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Coro",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.coro,
                                style = MaterialTheme.typography.bodyLarge,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Versos
                item.verses.forEach { (num, texto) ->
                    Row(modifier = Modifier.padding(vertical = 12.dp)) {
                        Text(
                            text = "$num.",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.width(28.dp)
                        )
                        Text(
                            text = texto,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Hino não encontrado", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}