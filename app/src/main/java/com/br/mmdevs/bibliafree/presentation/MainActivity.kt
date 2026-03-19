package com.br.mmdevs.bibliafree.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.br.mmdevs.bibliafree.navigations.LivrosNavigator
import com.br.mmdevs.bibliafree.navigations.livrosScreen
import com.br.mmdevs.bibliafree.navigations.versiculoScreen
import com.br.mmdevs.bibliafree.presentation.ui.theme.BibliaFreeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibliaFreeTheme {
                val rememberNavHostController = rememberNavController()
                NavHost(
                    navController = rememberNavHostController,
                    startDestination = LivrosNavigator,
                    enterTransition = { fadeIn(tween(300) ) },
                    exitTransition = { fadeOut(tween(300) ) }
                ){
                    livrosScreen(
                        navControler = rememberNavHostController
                    )
                    versiculoScreen(
                        navControler = rememberNavHostController
                    )
                }
            }
        }
    }
}

