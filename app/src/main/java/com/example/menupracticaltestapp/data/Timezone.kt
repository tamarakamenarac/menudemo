package com.example.menupracticaltestapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Timezone (
  @SerializedName("name") var name: String? = null,
  @SerializedName("offset") var offset: String? = null
) : Parcelable