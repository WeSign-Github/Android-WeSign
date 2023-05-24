package com.wesign.wesign.data.remote

import com.wesign.wesign.data.entity.CourseDetailResponse
import com.wesign.wesign.data.entity.CourseResponse
import com.wesign.wesign.data.entity.Lesson
import com.wesign.wesign.data.entity.request.RegisterRequest
import com.wesign.wesign.data.network.BaseApiService
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

typealias LessonResponse = Lesson

interface WeSignApiService : BaseApiService {

    @GET("/api/courses")
    suspend fun getCourses(): CourseResponse

    @GET("/api/courses/{id}")
    suspend fun getCourseDetail(@Path("id") id: Int): CourseDetailResponse

    @GET("/api/lesson/{id}")
    suspend fun getLesson(@Path("id") id: Int): LessonResponse

    @POST("/api/register")
    suspend fun register(@Body registerRequest: RegisterRequest)
}