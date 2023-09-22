package com.example.menupracticaltestapp.data

import com.google.gson.annotations.SerializedName

data class VenuesListResponse (
  @SerializedName("distance") var distance: Double? = null,
  @SerializedName("distance_in_miles") var distanceInMiles: Double? = null,
  @SerializedName("venue") var venue: Venue?  = Venue()
)