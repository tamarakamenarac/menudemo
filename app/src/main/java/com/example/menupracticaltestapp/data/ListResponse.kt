package com.example.menupracticaltestapp.data

import com.google.gson.annotations.SerializedName

data class ListResponse (
    @SerializedName("status") var status: String? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("data") var data: VenuesData? = VenuesData()
)