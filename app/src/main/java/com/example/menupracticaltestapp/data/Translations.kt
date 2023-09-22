package com.example.menupracticaltestapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Translations (
  @SerializedName("description") var description: String? = null,
  @SerializedName("kiosk_receipt_footer") var kioskReceiptFooter: String? = null,
  @SerializedName("welcome_message") var welcomeMessage: String? = null
): Parcelable