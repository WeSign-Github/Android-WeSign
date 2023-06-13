package com.wesign.wesign.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wesign.wesign.navigation.NestedGraph
import com.wesign.wesign.navigation.Screen
import com.wesign.wesign.sharedViewModel
import com.wesign.wesign.ui.analyze.AnalyzerRoute
import com.wesign.wesign.ui.analyze.AnalyzerViewModel
import com.wesign.wesign.ui.analyze.setting.SignLanguageSettingRoute

fun NavGraphBuilder.buildAnalyzerGraph(
    navController: NavController,
    onNotGrantedPermission: () -> Unit
) {

    navigation(
        route = NestedGraph.AnalyzerFeature.nestedRoute,
        startDestination = NestedGraph.AnalyzerFeature.startDestination.route
    ) {
        composable(Screen.AnalyzerCamera.route) {
            val viewModel = it.sharedViewModel<AnalyzerViewModel>(navController = navController)
            AnalyzerRoute(
                viewModel = viewModel,
                onNavigateUp = {
                    navController.navigateUp()
                },
                onSettingPressed = {
                    navController.navigate(Screen.AnalyzerSetting.route)
                },
                onNotGrantedPermission = onNotGrantedPermission,
            )
        }
        composable(Screen.AnalyzerSetting.route) {
            val viewModel = it.sharedViewModel<AnalyzerViewModel>(navController = navController)
            SignLanguageSettingRoute(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}