package com.wesign.wesign.data.entity


import com.google.gson.annotations.SerializedName

data class CompleteLessonResponse(
    @SerializedName("data")
    var data: String,
    @SerializedName("message")
    var message: String,
    @SerializedName("error")
    var error: Boolean
)