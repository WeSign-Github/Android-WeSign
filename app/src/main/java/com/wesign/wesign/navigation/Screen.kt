package com.wesign.wesign.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object RegisterInformation : Screen("registerInformation")

    object Home : Screen("home")
    object Profile : Screen("profile")

    object AnalyzerCamera : Screen("analyze")
    object AnalyzerSetting : Screen("analyze/setting")


    object TextToSignPick : Screen("text-to-sign/pick")
    object TextToSignGenerate : Screen("text-to-sign/generate")

    object Learning : Screen("learning")
    object CourseDetail : Screen("course/{id}") {
        fun createRoute(courseId: Int) = "course/$courseId"
    }
    object Lesson : Screen("lesson/{id}") {
        fun createRoute(lessonId: Int) = "lesson/$lessonId"
    }
    object LessonCamera : Screen("lesson/try") {

    }
}

