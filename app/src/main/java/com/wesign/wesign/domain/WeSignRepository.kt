package com.wesign.wesign.domain

import com.wesign.wesign.data.entity.CourseResponse
import com.wesign.wesign.domain.entity.RegisterUser
import kotlinx.coroutines.flow.Flow

interface WeSignRepository {

    fun registerUser(user: RegisterUser): String

    fun getCourses(): Flow<Resource<CourseResponse>>

}