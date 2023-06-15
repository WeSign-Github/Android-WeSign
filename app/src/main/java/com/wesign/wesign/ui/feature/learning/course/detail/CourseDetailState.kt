package com.wesign.wesign.ui.feature.learning.course.detail

import com.wesign.wesign.data.entity.CourseDetail


data class CourseDetailState(
    val isLoading: Boolean = false,
    val isTryAgain: Boolean = false,
    val course: CourseDetail = CourseDetail(-1, "Title", "sibi", "", "", "", emptyList()),
    val progressPercentage: Int = 0
)