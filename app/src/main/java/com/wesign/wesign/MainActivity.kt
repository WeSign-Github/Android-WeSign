package com.wesign.wesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wesign.wesign.navigation.Screen
import com.wesign.wesign.ui.home.HomeRoute
import com.wesign.wesign.ui.login.LoginRoute
import com.wesign.wesign.ui.register.RegisterInformationRoute
import com.wesign.wesign.ui.register.RegisterRoute
import com.wesign.wesign.ui.theme.WeSignTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeSignTheme {
                val navController = rememberNavController()
                var homeAsDefault by rememberSaveable { mutableStateOf(false) }

                NavHost(
                    navController = navController,
                    startDestination = if (homeAsDefault) Screen.Home.route else Screen.Login.route
                ) {
                    composable(Screen.Login.route) {
                        LoginRoute(
                            onRegisterPressed = {
                                navController.navigate(Screen.Register.route)
                            },
                            onLoginPressed = {
                                homeAsDefault = true
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(navController.graph.id) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable(Screen.Register.route) {
                        RegisterRoute(
                            onNavigateUp = {
                                navController.navigateUp()
                            },
                            onNext = {
                                navController.navigate(Screen.RegisterInformation.route) {

                                }
                            }
                        )
                    }

                    composable(Screen.RegisterInformation.route) {
                        RegisterInformationRoute {
                            navController.navigateUp()
                        }
                    }

                    composable(Screen.Home.route) {
                        HomeRoute()
                    }
                }
            }
        }
    }
}
