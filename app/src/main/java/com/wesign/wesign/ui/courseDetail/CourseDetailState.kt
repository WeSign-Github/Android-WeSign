package com.wesign.wesign.ui.courseDetail

import com.wesign.wesign.data.entity.CourseDetail


data class CourseDetailState(
    val isLoading: Boolean = false,
    val isTryAgain: Boolean = false,
    val course: CourseDetail = CourseDetail(-1, "Title", "Description", "", "", emptyList())
)