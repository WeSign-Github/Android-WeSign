package com.wesign.wesign.data.entity


import com.google.gson.annotations.SerializedName

data class WeSignRegisterResponse(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("error")
    var error: Boolean
) {
    data class Data(
        @SerializedName("id")
        var id: Int,
        @SerializedName("provider_id")
        var providerId: String,
        @SerializedName("provider_name")
        var providerName: String,
        @SerializedName("first_name")
        var firstName: String,
        @SerializedName("last_name")
        var lastName: String,
        @SerializedName("display_name")
        var displayName: String,
        @SerializedName("email")
        var email: String,
        @SerializedName("avatar")
        var avatar: String,
        @SerializedName("updatedAt")
        var updatedAt: String,
        @SerializedName("createdAt")
        var createdAt: String
    )
}