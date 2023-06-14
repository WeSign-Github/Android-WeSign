package com.wesign.wesign.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.wesign.wesign.navigation.NestedGraph
import com.wesign.wesign.navigation.Screen
import com.wesign.wesign.sharedViewModel
import com.wesign.wesign.ui.feature.learning.course.camera.LessonCameraRoute
import com.wesign.wesign.ui.feature.learning.course.detail.CourseDetailRoute
import com.wesign.wesign.ui.feature.learning.course.detail.CourseDetailViewModel
import com.wesign.wesign.ui.feature.learning.course.lesson.LessonRoute
import com.wesign.wesign.ui.feature.learning.course.lesson.LessonViewModel
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
            val viewModel: CourseDetailViewModel = it.sharedViewModel(navController)
            CourseDetailRoute(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onStartLearning = { startLessonId ->
                    navController.navigate(Screen.Lesson.createRoute(startLessonId))
                },
                idCourse = it.arguments?.getInt("id") ?: -1
            )
        }

        composable(Screen.Lesson.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                }
            )
        ) {
            val courseDetailViewModel: CourseDetailViewModel = it.sharedViewModel(navController)
            val viewModel: LessonViewModel = it.sharedViewModel(navController = navController)
            LessonRoute(
                courseDetailViewModel = courseDetailViewModel,
                viewModel = viewModel,
                lessonId = it.arguments?.getInt("id") ?: -1,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onCourseCompletePressed = {
                    navController.navigateUp()
//                    navController.backQueue.firstOrNull() { dest -> dest.destination.route == Screen.CourseDetail.route }?.let {
//
//                    }
                },
                onTryPressed = {
                    navController.navigate(Screen.LessonCamera.route)
                }
            )
        }

        composable(Screen.LessonCamera.route) {
            val courseDetailViewModel: CourseDetailViewModel =
                it.sharedViewModel(navController = navController)
            val lessonViewModel: LessonViewModel = it.sharedViewModel(navController = navController)

            LessonCameraRoute(
                lessonViewModel = lessonViewModel,
                courseDetailViewModel = courseDetailViewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
            )
        }
    }
}