package com.wesign.wesign.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wesign.wesign.navigation.NestedGraph
import com.wesign.wesign.navigation.Screen
import com.wesign.wesign.sharedViewModel
import com.wesign.wesign.ui.texttosign.TextToSignRoute
import com.wesign.wesign.ui.texttosign.TextToSignViewModel
import com.wesign.wesign.ui.texttosign.generate.GenerateSignRoute

fun NavGraphBuilder.buildTextToSignNavGraph(
    navController: NavController
) {
    navigation(
        route = NestedGraph.TextToSignFeature.nestedRoute,
        startDestination = NestedGraph.TextToSignFeature.startDestination.route,
    ) {
        composable(Screen.TextToSignPick.route) {
            val sharedViewModel =
                it.sharedViewModel<TextToSignViewModel>(navController = navController)
            TextToSignRoute(
                sharedViewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onGeneratePressed = { data ->
                    navController.navigate(Screen.TextToSignGenerate.route) {
                        navArgument("words") {

                        }
                    }
                }
            )
        }

        composable(Screen.TextToSignGenerate.route) {
            val sharedViewModel =
                it.sharedViewModel<TextToSignViewModel>(navController = navController)
            GenerateSignRoute(
                sharedViewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
            )
        }

    }
}