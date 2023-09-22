package com.example.menupracticaltestapp.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status") var status: String? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("data") var data: LoginData? = null
)