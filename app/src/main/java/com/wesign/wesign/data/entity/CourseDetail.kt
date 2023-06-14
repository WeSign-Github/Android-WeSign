package com.wesign.wesign.data.entity

import com.google.gson.annotations.SerializedName

data class CourseDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("lessons")
    val lessons: List<Lesson>
)
