package com.wesign.wesign.data.entity


import com.google.gson.annotations.SerializedName

data class CourseDetailResponse(
    @SerializedName("data")
    val data: CourseDetail
)