package com.wesign.wesign.ui.learning

import com.wesign.wesign.data.entity.Course

data class LearningState(
    val isLoading: Boolean = false,
    val courseList: List<Course> = emptyList(),
    val isTryAgain: Boolean = false
)