package com.pemmob.pawangdigimon.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.pawangdigimon.ui.screens.DetailScreen
import com.pemmob.pawangdigimon.ui.screens.HomeScreen
import com.pemmob.pawangdigimon.ui.viewmodel.DigimonViewModel

object Screen {
    const val HOME = "home"
    const val DETAIL = "detail/{digimonId}"

    fun createDetailRoute(id: Int) = "detail/$id"
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: DigimonViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HOME,
        modifier = modifier
    ) {
        composable(Screen.HOME) {
            val homeState by viewModel.homeUiState.collectAsState()
            HomeScreen(
                uiState = homeState,
                onDigimonClick = { digimonId ->
                    navController.navigate(Screen.createDetailRoute(digimonId))
                },
                onSearchQueryChange = { query ->
                    viewModel.searchDigimons(query)
                },
                onLoadMore = { viewModel.loadMoreDigimons() },
                onRetry = { viewModel.loadDigimons() }
            )
        }

        composable(
            route = Screen.DETAIL,
            arguments = listOf(navArgument("digimonId") { type = NavType.IntType })
        ) { backStackEntry ->
            val digimonId = backStackEntry.arguments?.getInt("digimonId") ?: 0
            val detailState by viewModel.detailUiState.collectAsState()

            DetailScreen(
                digimonId = digimonId,
                uiState = detailState,
                onLoadDetail = { id -> viewModel.loadDigimonDetail(id) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
