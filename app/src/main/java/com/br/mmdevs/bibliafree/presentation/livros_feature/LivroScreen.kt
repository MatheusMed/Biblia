package com.br.mmdevs.bibliafree.presentation.livros_feature


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.br.mmdevs.bibliafree.data.entity.LivroEntity
import com.br.mmdevs.bibliafree.presentation.livros_feature.states.LivroState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.br.mmdevs.bibliafree.domain.model.BookDto
import com.br.mmdevs.bibliafree.navigations.HarpaNavigator
import com.br.mmdevs.bibliafree.navigations.VersiculosNavigator
import com.br.mmdevs.bibliafree.presentation.ui.theme.corBranco
import com.br.mmdevs.bibliafree.presentation.ui.theme.corCard
import com.br.mmdevs.bibliafree.presentation.ui.theme.corLogo
import com.br.mmdevs.bibliafree.presentation.ui.theme.corPreto
import compose.icons.FontAwesomeIcons
import compose.icons.TablerIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Audible
import compose.icons.fontawesomeicons.brands.Github
import compose.icons.fontawesomeicons.brands.Leanpub
import compose.icons.fontawesomeicons.brands.Searchengin
import compose.icons.fontawesomeicons.brands.Sistrix
import compose.icons.tablericons.Music
import compose.icons.tablericons.Notebook
import compose.icons.tablericons.Search
import compose.icons.tablericons.X

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivroScreen(
    state: LivroState,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    navControler: NavController
) {

    var isSearchMode by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            onSearchChange("")
        }
    }

    Scaffold(
        containerColor = corLogo,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = corLogo,
                    titleContentColor = corPreto,
                    navigationIconContentColor = corPreto,
                ),
            title = {
                if (isSearchMode) {

                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = { Text("Buscar livro...", color = corPreto) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = corPreto,
                            unfocusedTextColor = corPreto,
                        )
                    )
                } else {
                    Text("Livros", fontWeight = FontWeight.Bold,color = corPreto)
                }
            },
            actions = {

                if (isSearchMode) {

                    IconButton(onClick = {
                        isSearchMode = false
                        onSearchChange("")
                    }) {
                        Icon(TablerIcons.X, "", tint = corPreto)
                    }
                } else {

                    IconButton(onClick = { isSearchMode = true }) {
                        Icon(TablerIcons.Search, "", tint = corPreto, modifier = Modifier.size(28.dp))
                    }
                }

                IconButton(onClick = { navControler.navigate(HarpaNavigator) }) {
                    Icon(TablerIcons.Music, "", tint = corPreto, modifier = Modifier.size(28.dp))
                }

            }
        ) },
        content = {
            paddingValues -> Box(
            modifier = Modifier.padding(paddingValues)
            ){
            when(state){
                is LivroState.Error -> {
                    ErrorScreen(state.message)
                }
                LivroState.Loading -> {
                    LoadingScreen()
                }
                is LivroState.Success -> {
                    if (state.livros.isEmpty() && searchQuery.isNotEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Nenhum livro encontrado")
                        }
                    } else {
                        SuccessScreen(
                            state.livros,
                            navControler
                        ) { item ->
                            val sigla = item.abbrev
                                ?.trim()
                                ?.lowercase()
                                ?.replace(" ", "")

                            navControler.navigate(
                                VersiculosNavigator(sigla ?: "")
                            )

                        }
                    }
                }
            }
        }
        }
    )

}


@Composable
fun LoadingScreen(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message:String){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(message, color = Color.Red)
    }
}

@Composable
fun SuccessScreen(
    livros: List<BookDto>,
    navControler: NavController,
   onCLick: (item: BookDto) -> Unit
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(livros){item->
            ItemCardLivros(item.abbrev) {
                onCLick(item)
            }
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier.fillMaxWidth()
//
//
//            ) {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(60.dp)
//
//                        .clickable {
//                           onCLick(item)
//                        }
//                        .padding(6.dp),
//
//                    colors = CardDefaults.cardColors(
//                        containerColor = corBranco,
//                    )
//                ) {
//
//                        Text(
//                            getNomeLivro(item.abbrev),
//                            color = Color.Black,
//                            textAlign = TextAlign.Center,
//
//                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
//                        )
//
//                }
//            }
        }
    }




}



@Composable
fun ItemCardLivros(
    titulo: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()

            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { onClick() },

        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),

        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(
            containerColor = corBranco
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = getNomeLivro(titulo),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,

                overflow = TextOverflow.Ellipsis
            )
        }

    }
}
fun getNomeLivro(sigla: String?): String {
    return when (sigla?.trim()) {
        "gn" -> "Gênesis"
        "ex" -> "Êxodo"
        "lv" -> "Levítico"
        "nm" -> "Números"
        "dt" -> "Deuteronômio"
        "js" -> "Josué"
        "jz" -> "Juízes"
        "rt" -> "Rute"
        "1sm" -> "1 Samuel"
        "2sm" -> "2 Samuel"
        "1 rs" -> "1 Reis"
        "2rs" -> "2 Reis"
        "1cr" -> "1 Crônicas"
        "2cr" -> "2 Crônicas"
        "ed" -> "Esdras"
        "ne" -> "Neemias"
        "et" -> "Ester"
        "jó" -> "Jó"
        "sl" -> "Salmos"
        "pv" -> "Provérbios"
        "ec" -> "Eclesiastes"
        "ct" -> "Cânticos"
        "is" -> "Isaías"
        "jr" -> "Jeremias"
        "lm" -> "Lamentações"
        "ez" -> "Ezequiel"
        "dn" -> "Daniel"
        "os" -> "Oséias"
        "jl" -> "Joel"
        "am" -> "Amós"
        "ob" -> "Obadias"
        "jn" -> "Jonas"
        "mq" -> "Miquéias"
        "na" -> "Naum"
        "hc" -> "Habacuque"
        "sf" -> "Sofonias"
        "ag" -> "Ageu"
        "zc" -> "Zacarias"
        "ml" -> "Malaquias"

        "mt" -> "Mateus"
        "mc" -> "Marcos"
        "lc" -> "Lucas"
        "jo" -> "João"
        "at" -> "Atos"
        "rm" -> "Romanos"
        "1co" -> "1 Coríntios"
        "2co" -> "2 Coríntios"
        "gl" -> "Gálatas"
        "ef" -> "Efésios"
        "fp" -> "Filipenses"
        "cl" -> "Colossenses"
        "1ts" -> "1 Tessalonicenses"
        "2ts" -> "2 Tessalonicenses"
        "1tm" -> "1 Timóteo"
        "2tm" -> "2 Timóteo"
        "tt" -> "Tito"
        "fm" -> "Filemom"
        "hb" -> "Hebreus"
        "tg" -> "Tiago"
        "1pe" -> "1 Pedro"
        "2pe" -> "2 Pedro"
        "1jo" -> "1 João"
        "2jo" -> "2 João"
        "3jo" -> "3 João"
        "jd" -> "Judas"
        "ap" -> "Apocalipse"

        else -> "Livro desconhecido"
    }
}