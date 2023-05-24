package com.wesign.wesign.data.entity.request


import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("provider_id")
    var providerId: String,
    @SerializedName("provider_name")
    var providerName: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("avatar")
    var avatar: String
)