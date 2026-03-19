package com.br.mmdevs.bibliafree.presentation.livros_feature


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.br.mmdevs.bibliafree.data.entity.LivroEntity
import com.br.mmdevs.bibliafree.presentation.livros_feature.states.LivroState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.br.mmdevs.bibliafree.domain.model.BookDto
import com.br.mmdevs.bibliafree.navigations.VersiculosNavigator
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Audible
import compose.icons.fontawesomeicons.brands.Github
import compose.icons.fontawesomeicons.brands.Leanpub
import compose.icons.fontawesomeicons.brands.Searchengin
import compose.icons.fontawesomeicons.brands.Sistrix

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivroScreen(
    state: LivroState,
    modifier: Modifier = Modifier,
    navControler: NavController
) {
    Scaffold(
        topBar = { TopAppBar(
            title = {
                Text("Livros")
            },
            actions = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        FontAwesomeIcons.Brands.Sistrix,
                        "",

                        modifier = modifier.padding(horizontal = 3.dp)
                            .size(28.dp)
                    )
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
                    SuccessScreen(state.livros,navControler)
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
    navControler: NavController
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(livros){item->

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()

            ) {
                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val sigla = item.abbrev
                                ?.trim()
                                ?.lowercase()
                                ?.replace(" ", "")

                            navControler.navigate(
                                VersiculosNavigator(sigla ?: "")
                            )
                        }
                        .padding(6.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            getNomeLivro(item.abbrev),
                            color = Color.Black,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Icon(
                            FontAwesomeIcons.Brands.Audible,
                            "",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
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