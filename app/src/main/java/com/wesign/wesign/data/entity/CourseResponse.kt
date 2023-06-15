package com.wesign.wesign.data.entity


import com.google.gson.annotations.SerializedName

data class CourseResponse(
    @SerializedName("data")
    val data: List<Course>
)
