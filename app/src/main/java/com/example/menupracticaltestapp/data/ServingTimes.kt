package com.example.menupracticaltestapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServingTimes (
  @SerializedName("id") var id: Int? = null,
  @SerializedName("type_id") var typeId: Int? = null,
  @SerializedName("reference_type") var referenceType: String? = null,
  @SerializedName("time_from") var timeFrom: String? = null,
  @SerializedName("time_to") var timeTo: String? = null,
  @SerializedName("delivery_at") var deliveryAt: String? = null,
  @SerializedName("days") var days: Array<Int> = arrayOf()
): Parcelable