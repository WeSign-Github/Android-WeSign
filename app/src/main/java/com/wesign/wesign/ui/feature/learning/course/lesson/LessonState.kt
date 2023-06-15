package com.wesign.wesign.ui.feature.learning.course.lesson

import com.wesign.wesign.data.entity.LessonResponse

data class LessonState(
    val lesson: LessonResponse.Data = LessonResponse.Data(-1, 0, "Meja", "", "", "", 1),
    val isLoading: Boolean = false,
    val isChallengeComplete: Boolean = false
)
