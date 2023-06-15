package com.wesign.wesign.data.entity


import com.google.gson.annotations.SerializedName

data class LessonResponse(
    @SerializedName("data")
    var data: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("error")
    var error: Boolean
) {
    data class Data(
        @SerializedName("id")
        var id: Int,
        @SerializedName("course_id")
        var courseId: Int,
        @SerializedName("title")
        var title: String,
        @SerializedName("thumbnail")
        var thumbnail: String,
        @SerializedName("createdAt")
        var createdAt: String,
        @SerializedName("updatedAt")
        var updatedAt: String,
        @SerializedName("next_lesson_id")
        var nextLessonId: Int?
    )
}