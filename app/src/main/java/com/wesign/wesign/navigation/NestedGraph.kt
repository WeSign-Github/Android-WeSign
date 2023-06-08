package com.wesign.wesign.navigation


sealed class NestedGraph(val nestedRoute: String, val startDestination: Screen) {
    object LearningFeature : NestedGraph("/feature/learning", Screen.Learning)
    object TextToSignFeature : NestedGraph("/feature/text-to-sign", Screen.TextToSignPick)
}