package com.example.menupracticaltestapp.screens.listOfVenues

import com.example.menupracticaltestapp.data.VenuesListResponse

sealed class ListOfVenuesViewState {
    data class Success(val listOfVenues: List<VenuesListResponse>): ListOfVenuesViewState()
    data class Error(val error: String): ListOfVenuesViewState()
    object Loading: ListOfVenuesViewState()
}