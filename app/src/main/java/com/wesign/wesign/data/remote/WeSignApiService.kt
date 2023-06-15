package com.wesign.wesign.data.remote

import com.wesign.wesign.data.entity.CompleteLessonResponse
import com.wesign.wesign.data.entity.CourseDetailResponse
import com.wesign.wesign.data.entity.CourseResponse
import com.wesign.wesign.data.entity.LessonResponse
import com.wesign.wesign.data.entity.SelfUserResponse
import com.wesign.wesign.data.entity.TextToSignResponse
import com.wesign.wesign.data.entity.WeSignRegisterResponse
import com.wesign.wesign.data.entity.request.RegisterRequest
import com.wesign.wesign.data.network.BaseApiService
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WeSignApiService : BaseApiService {

    @GET("/api/users/me")
    suspend fun getCurrentUser(): SelfUserResponse

    @GET("/api/courses")
    suspend fun getCourses(): CourseResponse

    @GET("/api/courses/{id}")
    suspend fun getCourseDetail(@Path("id") id: Int): CourseDetailResponse

    @GET("/api/lessons/{id}")
    suspend fun getLesson(@Path("id") id: Int): LessonResponse
    @POST("/api/lessons/{id}/complete")
    suspend fun completeLesson(@Path("id") id: Int): CompleteLessonResponse

    @GET("/api/text-to-sign")
    suspend fun getTextToSign(@Query("text") text: String = ""): TextToSignResponse

    @POST("/api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): WeSignRegisterResponse
}