package com.wesign.wesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wesign.wesign.navigation.Screen
import com.wesign.wesign.ui.analyze.AnalyzerRoute
import com.wesign.wesign.ui.home.HomeRoute
import com.wesign.wesign.ui.login.LoginRoute
import com.wesign.wesign.ui.register.RegisterInformationRoute
import com.wesign.wesign.ui.register.RegisterRoute
import com.wesign.wesign.ui.theme.WeSignTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeSignTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                var homeAsDefault by rememberSaveable { mutableStateOf(false) }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { contentPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (homeAsDefault) Screen.Home.route else Screen.Login.route,
                        modifier = Modifier.padding(contentPadding),
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
                            HomeRoute(onAnalyzePressed = {
                                navController.navigate(Screen.AnalyzerCamera.route)
                            })
                        }

                        composable(Screen.AnalyzerCamera.route) {
                            AnalyzerRoute(
                                onNavigateUp = {
                                    navController.navigateUp()
                                },
                                onNotGrantedPermission = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Need Camera Permission")
                                    }
                                }
                            )
                        }
                    }
                }


            }
        }
    }
}
