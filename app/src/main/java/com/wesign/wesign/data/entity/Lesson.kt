package com.wesign.wesign.data.entity

import com.google.gson.annotations.SerializedName

data class Lesson(
    @SerializedName("id")
    val id: Int,
    @SerializedName("course_id")
    val courseId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("completed")
    val completed: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)
