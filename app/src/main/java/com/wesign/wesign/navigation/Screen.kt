package com.wesign.wesign.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object RegisterInformation : Screen("registerInformation")

    object Home : Screen("home")
    object Profile : Screen("profile")

    object AnalyzerCamera : Screen("analyze")
}
