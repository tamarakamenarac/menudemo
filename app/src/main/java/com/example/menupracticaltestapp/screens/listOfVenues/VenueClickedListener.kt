package com.example.menupracticaltestapp.screens.listOfVenues

import com.example.menupracticaltestapp.data.VenuesListResponse

interface VenueClickedListener {
    fun venueClicked(item: VenuesListResponse)
}