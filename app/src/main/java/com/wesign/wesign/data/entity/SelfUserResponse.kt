package com.wesign.wesign.data.entity


import com.google.gson.annotations.SerializedName

data class SelfUserResponse(
    @SerializedName("data")
    var data: User? = null
) {
    data class User(
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
        @SerializedName("is_verified")
        var isVerified: Boolean,
        @SerializedName("avatar")
        var avatar: String,
        @SerializedName("createdAt")
        var createdAt: String,
        @SerializedName("updatedAt")
        var updatedAt: String
    )
}