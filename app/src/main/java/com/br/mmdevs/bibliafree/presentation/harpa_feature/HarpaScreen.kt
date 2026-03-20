package com.br.mmdevs.bibliafree.presentation.harpa_feature

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.br.mmdevs.bibliafree.presentation.harpa_feature.state.HarpaState
import com.br.mmdevs.bibliafree.presentation.ui.theme.corBranco
import com.br.mmdevs.bibliafree.presentation.ui.theme.corLogo
import com.br.mmdevs.bibliafree.presentation.ui.theme.corPreto
import compose.icons.FeatherIcons
import compose.icons.TablerIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.X
import compose.icons.tablericons.Search


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarpaScreen(
    state: HarpaState,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onHinoClick: (String) -> Unit,
    navController: NavController
) {
    Scaffold(
        containerColor = corLogo,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(FeatherIcons.ArrowLeft,"")
                    }

                },
                title = {
                    Text(
                        text = "Hinos da Harpa",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = corLogo,
                    titleContentColor = corPreto,
                    navigationIconContentColor = corPreto,
                ),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Buscar por número ou nome...", color = corPreto) },
            leadingIcon = { Icon(TablerIcons.Search, "", tint = corPreto, modifier = Modifier.size(18.dp)) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(FeatherIcons.X, "", tint = corPreto)
                    }
                }
            },
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedContainerColor = corBranco,
                unfocusedContainerColor = corBranco,
                focusedTextColor = corPreto,
                unfocusedTextColor = corPreto,
                cursorColor = corPreto,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true,
            shape = CircleShape,

        )


        Box(
            modifier = Modifier.weight(1f)
        ) {
            when (state) {
                is HarpaState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is HarpaState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.hinos) { hino ->
                            val numero = hino.hino.split(" - ").firstOrNull() ?: ""

                            HinoCard(
                                tituloCompleto = hino.hino,
                                numero = numero,
                                onClick = { onHinoClick(numero) }
                            )
                        }
                    }
                }

                is HarpaState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Ops! Algo deu errado", color = Color.Gray)
                        Text(text = state.message, color = Color.Red, fontSize = 12.sp)
                    }
                }
            }
        }
    }
    }
}

@Composable
fun HinoCard(
    tituloCompleto: String,
    numero: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),

        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = corBranco
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = numero,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))


            val nomeHino = tituloCompleto.split(" - ").lastOrNull() ?: tituloCompleto
            Text(
                text = nomeHino,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}