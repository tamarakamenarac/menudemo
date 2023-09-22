package com.example.menupracticaltestapp.data

import com.google.gson.annotations.SerializedName

data class Token (
    @SerializedName("value") var value: String? = null,
    @SerializedName("ttl") var ttl: Int? = null,
    @SerializedName("refresh_ttl") var refreshTtl: Int? = null,
    @SerializedName("issued_token_type") var issuedTokenType: String? = null,
    @SerializedName("token_type") var tokenType: String? = null
)