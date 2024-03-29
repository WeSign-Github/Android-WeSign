package com.wesign.wesign.ui.feature.learning.course.detail

import com.wesign.wesign.data.entity.CourseDetail
import com.wesign.wesign.data.entity.Lesson
import com.wesign.wesign.data.entity.LessonResponse


data class CourseDetailState(
    val isLoading: Boolean = false,
    val isTryAgain: Boolean = false,
    val course: CourseDetail = CourseDetail(-1, "Title", "sibi", "", "", "", emptyList()),
    val lessonList: List<LessonResponse.Data> = emptyList(),
    val progressPercentage: Int = 0
)