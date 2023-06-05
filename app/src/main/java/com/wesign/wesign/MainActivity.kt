package com.wesign.wesign

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.IdTokenListener
import com.wesign.wesign.core.SessionManager
import com.wesign.wesign.domain.WeSignRepository
import com.wesign.wesign.navigation.Screen
import com.wesign.wesign.ui.analyze.AnalyzerRoute
import com.wesign.wesign.ui.courseDetail.CourseDetailRoute
import com.wesign.wesign.ui.home.HomeRoute
import com.wesign.wesign.ui.learning.LearningRoute
import com.wesign.wesign.ui.login.LoginRoute
import com.wesign.wesign.ui.profile.ProfileRoute
import com.wesign.wesign.ui.register.RegisterInformationRoute
import com.wesign.wesign.ui.register.RegisterRoute
import com.wesign.wesign.ui.theme.WeSignTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var weSignRepository: WeSignRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val scope = rememberCoroutineScope()
            LaunchedEffect(Unit) {

                firebaseAuth.addAuthStateListener {
                    scope.launch {
                        Log.d("MainActivity", "Auth State Trigger...")
                        it.currentUser?.let { user ->
                            val token = it.currentUser?.getIdToken(false)?.await()
                            token?.let { tokenResult ->
                                sessionManager.saveToken(tokenResult.token ?: "")
                            } ?: run {
                                sessionManager.saveToken("")
                            }

                        }
                    }
                }

                firebaseAuth.addIdTokenListener(
                    IdTokenListener { currentFirbaseAuth ->
                        Log.d("MainActivity", "Token Listener Trigger...")
                        scope.launch {
                            val token = currentFirbaseAuth.currentUser?.getIdToken(false)?.await()
                            token?.let { tokenResult ->
                                Log.d("MainActivity", "Token ${tokenResult.token}")
                                sessionManager.saveToken(tokenResult.token ?: "")
                            } ?: run {
                                sessionManager.saveToken("")
                            }

                        }
                    },
                )
            }

            WeSignTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                var homeAsDefault by rememberSaveable { mutableStateOf(false) }
                val localContext = LocalContext.current

                runBlocking {
                    homeAsDefault = !sessionManager.getToken().first().isNullOrEmpty()
                }



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
                                onLoginSuccess = {
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
                                        popUpTo(navController.graph.id) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable(Screen.RegisterInformation.route) {
                            RegisterInformationRoute(
                                onNavigateUp = navController::navigateUp,
                                onRegisterSuccess = {
                                    navController.navigate(navController.graph.startDestinationRoute!!) {
                                        popUpTo(navController.graph.id) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }

                        composable(Screen.Home.route) {
                            HomeRoute(
                                onAnalyzePressed = {
                                    navController.navigate(Screen.AnalyzerCamera.route)
                                },
                                onProfilePressed = {
                                    navController.navigate(Screen.Profile.route)
                                },
                                onLearningPressed = {
                                    navController.navigate(Screen.Learning.route)
                                }
                            )
                        }

                        composable(Screen.AnalyzerCamera.route) {
//                            startActivity(Intent(applicationContext, AnalyzerActivity::class.java))
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

                        composable(Screen.Profile.route) {
                            ProfileRoute(
                                onNavigateUp = {
                                    navController.navigateUp()
                                },
                                onLogoutPressed = {
                                    firebaseAuth.signOut()
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(navController.graph.id) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onUserEmpty = {
                                    navController.navigate(Screen.RegisterInformation.route) {
                                    }
                                }
                            )
                        }

                        composable(Screen.Learning.route) {
                            LearningRoute(
                                onNavigateUp = {
                                    navController.navigateUp()
                                },
                                onItemClicked = { id ->
                                    navController.navigate(Screen.CourseDetail.createRoute(id))
                                }
                            )
                        }

                        composable(
                            Screen.CourseDetail.route,
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                    nullable = false
                                }
                            ),
                        ) {
                            CourseDetailRoute(
                                onNavigateBack = {
                                    navController.navigateUp()
                                },
                                idCourse = it.arguments?.getInt("id") ?: -1
                            )
                        }
                    }
                }
            }
        }
    }
}
