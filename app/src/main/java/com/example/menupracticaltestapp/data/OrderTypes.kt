package com.example.menupracticaltestapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderTypes (
  @SerializedName("id") var id: Int? = null,
  @SerializedName("type_id") var typeId: Int? = null,
  @SerializedName("reference_type") var referenceType: String? = null,
  @SerializedName("state") var state: Int? = null,
  @SerializedName("is_table_supported") var isTableSupported: Boolean? = null,
  @SerializedName("external_channel_only") var externalChannelOnly: Boolean? = null,
  @SerializedName("tip_default") var tipDefault: Int? = null,
  @SerializedName("tip_max") var tipMax: Int? = null,
  @SerializedName("area_id") var areaId: Int? = null,
  @SerializedName("pos_id") var posId: String? = null,
  @SerializedName("table_pos_id") var tablePosId: String? = null,
  @SerializedName("singular_point_id") var singularPointId: Int? = null
): Parcelable

enum class OrderTypesEnum(var type: String) {
  PREORDER("OrderTypePreorder"),
  DELIVERY("OrderTypeDelivery"),
  TAKEOUT("OrderTypeTakeOut")
}