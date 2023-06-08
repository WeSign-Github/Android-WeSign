package com.wesign.wesign.data.entity


import com.google.gson.annotations.SerializedName

data class TextToSignResponse(
    @SerializedName("data")
    var data: List<SignWord>,
    @SerializedName("message")
    var message: String,
    @SerializedName("error")
    var error: Boolean
) {
    data class SignWord(
        @SerializedName("id")
        var id: Int,
        @SerializedName("word")
        var word: String,
        @SerializedName("image")
        var image: String,
        @SerializedName("createdAt")
        var createdAt: String,
        @SerializedName("updatedAt")
        var updatedAt: String
    )
}