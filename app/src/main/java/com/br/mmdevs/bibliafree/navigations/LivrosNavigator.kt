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
import com.br.mmdevs.bibliafree.presentation.livros_feature.LivroScreen
import com.br.mmdevs.bibliafree.presentation.livros_feature.LivrosViewModel
import com.br.mmdevs.bibliafree.presentation.livros_feature.VersiculoScreen
import kotlinx.serialization.Serializable

@Serializable
data object LivrosNavigator

@Serializable
data class VersiculosNavigator(
    val abbrev: String
)


fun NavGraphBuilder.livrosScreen(
    navControler: NavController
){

    composable<LivrosNavigator> {
        val viewModel = hiltViewModel<LivrosViewModel>()
        val state by viewModel.stateLivros
        LivroScreen(
            state = state,
            navControler = navControler

        )
    }
}

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