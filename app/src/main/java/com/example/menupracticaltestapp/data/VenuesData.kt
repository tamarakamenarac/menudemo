package com.example.menupracticaltestapp.data

import com.google.gson.annotations.SerializedName

data class VenuesData(
    @SerializedName("venues") var venues : ArrayList<VenuesListResponse> = arrayListOf()
)