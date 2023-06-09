package com.wesign.wesign.domain

import com.wesign.wesign.data.entity.CompleteLessonResponse
import com.wesign.wesign.data.entity.CourseDetailResponse
import com.wesign.wesign.data.entity.CourseResponse
import com.wesign.wesign.data.entity.LessonResponse
import com.wesign.wesign.data.entity.SelfUserResponse
import com.wesign.wesign.data.entity.TextToSignResponse
import com.wesign.wesign.data.entity.WeSignRegisterResponse
import com.wesign.wesign.data.entity.request.RegisterRequest
import kotlinx.coroutines.flow.Flow

interface WeSignRepository {

    fun registerUser(user: RegisterRequest): Flow<Resource<WeSignRegisterResponse>>

    fun getCurrentUser(): Flow<Resource<SelfUserResponse>>

    fun getCourses(): Flow<Resource<CourseResponse>>
    fun getDetailCourses(id: Int): Flow<Resource<CourseDetailResponse>>
    fun getLesson(id: Int): Flow<Resource<LessonResponse>>

    fun completeLesson(id: Int): Flow<Resource<CompleteLessonResponse>>

    fun getAllSignToTextWord(): Flow<Resource<TextToSignResponse>>
    fun getSignToText(text: String): Flow<Resource<TextToSignResponse>>

}