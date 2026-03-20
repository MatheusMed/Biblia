package com.br.mmdevs.bibliafree.navigations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.br.mmdevs.bibliafree.presentation.harpa_feature.DetalheHinoScreen
import com.br.mmdevs.bibliafree.presentation.harpa_feature.HarpaScreen
import com.br.mmdevs.bibliafree.presentation.harpa_feature.HarpaViewModel
import com.br.mmdevs.bibliafree.presentation.livros_feature.LivroScreen
import com.br.mmdevs.bibliafree.presentation.livros_feature.LivrosViewModel
import com.br.mmdevs.bibliafree.presentation.livros_feature.VersiculoScreen

import kotlinx.serialization.Serializable

@Serializable
data object LivrosNavigator




fun NavGraphBuilder.livrosScreen(
    navControler: NavController
){

    composable<LivrosNavigator> {
        val viewModel = hiltViewModel<LivrosViewModel>()
        val state = viewModel.stateLivros
        val query by viewModel.searchQuery
        LivroScreen(
            state = state,
            searchQuery = query,
            onSearchChange = viewModel::onSearchQueryChange,
            navControler = navControler,


        )
    }
}

@Serializable
data object HarpaNavigator

@Serializable
data class DetalheHinoNavigator(val hinoId: String)

fun NavGraphBuilder.harpaScreen(navController: NavController) {

    composable<HarpaNavigator> {
        val viewModel = hiltViewModel<HarpaViewModel>()

        val state = viewModel.stateHinos // Agora pega o getter filtrado
        val searchQuery by viewModel.searchQuery // Observa o texto da busca
        HarpaScreen(
            state = state,
            searchQuery = searchQuery,
            onSearchChange = viewModel::onSearchQueryChange,
            onHinoClick = { id ->

                navController.navigate(DetalheHinoNavigator(hinoId = id))
            },
            navController=navController
        )
    }


    composable<DetalheHinoNavigator> { backStackEntry ->
        val args = backStackEntry.toRoute<DetalheHinoNavigator>()
        val viewModel = hiltViewModel<HarpaViewModel>()


        val hino = viewModel.getHinoById(args.hinoId)

        DetalheHinoScreen(hino = hino, onBack = { navController.popBackStack() })
    }
}
@Serializable
data class VersiculosNavigator(
    val abbrev: String
)
fun NavGraphBuilder.versiculoScreen(
    navControler: NavController
){
    composable<VersiculosNavigator> { backStackEntry ->

        val args = backStackEntry.toRoute<VersiculosNavigator>()

        val viewModel = hiltViewModel<LivrosViewModel>()


        LaunchedEffect(Unit) {
            viewModel.getChaptersByAbbrev(args.abbrev)
        }

        val chapters by viewModel.chapters.collectAsState()
        val fontSize by viewModel.fontSize.collectAsState()

        VersiculoScreen(
            chapters = chapters,
            navControler = navControler,
            abbrev = args.abbrev,
            fontSize = fontSize,
            increaseFont = viewModel::increaseFont,
            decreaseFont = viewModel::decresentFont,
            viewModel = viewModel

        )
    }
}
