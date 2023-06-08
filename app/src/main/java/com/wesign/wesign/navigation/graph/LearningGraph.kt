package com.wesign.wesign.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wesign.wesign.navigation.NestedGraph
import com.wesign.wesign.navigation.Screen
import com.wesign.wesign.ui.feature.learning.course.detail.CourseDetailRoute
import com.wesign.wesign.ui.feature.learning.course.lesson.LessonRoute
import com.wesign.wesign.ui.feature.learning.learning.LearningRoute

internal fun NavGraphBuilder.buildLearningNavGraph(
    navController: NavController
) {
    navigation(
        route = NestedGraph.LearningFeature.nestedRoute,
        startDestination = NestedGraph.LearningFeature.startDestination.route
    ) {
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
                onStartLearning = {
                    navController.navigate(Screen.Lesson.route)
                },
                idCourse = it.arguments?.getInt("id") ?: -1
            )
        }

        composable(Screen.Lesson.route) {
            LessonRoute()
        }
    }
}