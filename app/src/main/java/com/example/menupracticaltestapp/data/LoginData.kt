package com.example.menupracticaltestapp.data

import com.google.gson.annotations.SerializedName

data class LoginData (
    @SerializedName("token") var token: Token? = null,
    @SerializedName("dlc_access_token") var dlcAccessToken: String? = null
)