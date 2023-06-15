package com.wesign.wesign.data

import android.util.Log
import com.wesign.wesign.data.entity.CompleteLessonResponse
import com.wesign.wesign.data.entity.CourseDetailResponse
import com.wesign.wesign.data.entity.CourseResponse
import com.wesign.wesign.data.entity.LessonResponse
import com.wesign.wesign.data.entity.SelfUserResponse
import com.wesign.wesign.data.entity.TextToSignResponse
import com.wesign.wesign.data.entity.WeSignRegisterResponse
import com.wesign.wesign.data.entity.request.RegisterRequest
import com.wesign.wesign.data.remote.WeSignApiService
import com.wesign.wesign.domain.Resource
import com.wesign.wesign.domain.WeSignRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeSignRepositoryImpl @Inject constructor(
    private val apiService: WeSignApiService
) : WeSignRepository {
    override fun registerUser(user: RegisterRequest): Flow<Resource<WeSignRegisterResponse>> =
        flow {
            emit(Resource.Loading)
            try {
                val result = apiService.register(user)
                emit(Resource.Success(result))
            } catch (ex: Exception) {
                emit(Resource.Error(ex))
                Log.e("WeSignRepo", ex.message.toString())
            }
        }

    override fun getCurrentUser(): Flow<Resource<SelfUserResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.getCurrentUser()
            delay(2000) // TODO Delete this later
            emit(Resource.Success(result))
        } catch (ex: Exception) {
            emit(Resource.Error(ex))
            Log.e("WeSignRepo", ex.message.toString())
        }
    }

    override fun getCourses(): Flow<Resource<CourseResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.getCourses()
            emit(Resource.Success(result))
        } catch (ex: Exception) {
            emit(Resource.Error(ex))
            Log.e("WeSignRepo", ex.message.toString())
        }
    }

    override fun getDetailCourses(id: Int): Flow<Resource<CourseDetailResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.getCourseDetail(id)
            emit(Resource.Success(result))
        } catch (ex: Exception) {
            emit(Resource.Error(ex))
            Log.e("WeSignRepo", ex.message.toString())
        }
    }

    override fun getLesson(id: Int): Flow<Resource<LessonResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.getLesson(id)
            emit(Resource.Success(result))
        } catch (ex: Exception) {
            emit(Resource.Error(ex))
            Log.e("WeSignRepo", ex.message.toString())
        }
    }

    override fun completeLesson(id: Int): Flow<Resource<CompleteLessonResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.completeLesson(id)
            emit(Resource.Success(result))
        } catch (ex: Exception) {
            emit(Resource.Error(ex))
            Log.e("WeSignRepo", ex.message.toString())
        }
    }

    override fun getAllSignToTextWord(): Flow<Resource<TextToSignResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.getTextToSign()
            emit(Resource.Success(result))
        } catch (ex: Exception) {
            emit(Resource.Error(ex))
            Log.e("WeSignRepo", ex.message.toString())
        }
    }

    override fun getSignToText(text: String): Flow<Resource<TextToSignResponse>> = flow {
        emit(Resource.Loading)
        try {
            val result = apiService.getTextToSign(text)
            emit(Resource.Success(result))
        } catch (ex: Exception) {
            emit(Resource.Error(ex))
            Log.e("WeSignRepo", ex.message.toString())
        }
    }
}