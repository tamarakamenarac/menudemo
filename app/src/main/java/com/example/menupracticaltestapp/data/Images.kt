package com.example.menupracticaltestapp.data

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize

@Parcelize
data class Images(
    @JsonProperty("thumbnail_medium") val thumbnail: String?,
): Parcelable