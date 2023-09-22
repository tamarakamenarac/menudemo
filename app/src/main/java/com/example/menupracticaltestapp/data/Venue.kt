package com.example.menupracticaltestapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Venue (
  @SerializedName("id") var id: Int? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("code") var code: String? = null,
  @SerializedName("state") var state: Int? = null,
  @SerializedName("timezone") var timezone: Timezone? = Timezone(),
  @SerializedName("description") var description: String? = null,
  @SerializedName("address") var address: String? = null,
  @SerializedName("city") var city: String? = null,
  @SerializedName("zip") var zip: String? = null,
  @SerializedName("latitude") var latitude: Double? = null,
  @SerializedName("longitude") var longitude: Double? = null,
  @SerializedName("serving_times") var servingTimes: ArrayList<ServingTimes> = arrayListOf(),
  @SerializedName("is_open") var isOpen: Boolean = false,
  @SerializedName("will_open") var willOpen: Boolean = false,
  @SerializedName("translations") var translations: Translations? = Translations(),
  @SerializedName("days_in_advance") var daysInAdvance: Int? = null,
  @SerializedName("order_types") var orderTypes: ArrayList<OrderTypes> = arrayListOf()
) : Parcelable