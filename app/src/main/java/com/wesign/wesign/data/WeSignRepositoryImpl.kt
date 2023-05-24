package com.wesign.wesign.data

import android.util.Log
import com.wesign.wesign.data.entity.CourseResponse
import com.wesign.wesign.data.remote.WeSignApiService
import com.wesign.wesign.domain.Resource
import com.wesign.wesign.domain.WeSignRepository
import com.wesign.wesign.domain.entity.RegisterUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeSignRepositoryImpl @Inject constructor(
    private val apiService: WeSignApiService
) : WeSignRepository {
    override fun registerUser(user: RegisterUser): String {
        return ""
    }

    override fun getCourses(): Flow<Resource<CourseResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.getCourses()
            delay(3000) // TODO Delete this later
            emit(Resource.Success(result))
        } catch (ex: Exception) {
            emit(Resource.Error(ex))
            Log.e("WeSignRepo", ex.message.toString())
        }
    }
}