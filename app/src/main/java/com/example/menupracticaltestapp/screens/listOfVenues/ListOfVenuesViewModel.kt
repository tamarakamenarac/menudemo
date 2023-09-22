package com.example.menupracticaltestapp.screens.listOfVenues

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menupracticaltestapp.helpers.Failure
import com.example.menupracticaltestapp.helpers.Success
import com.example.menupracticaltestapp.repo.RemoteRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListOfVenuesViewModel(
    private val repo: RemoteRepo
) : ViewModel() {

    val viewState: MutableLiveData<ListOfVenuesViewState> = MutableLiveData()
    
    private var latitude: Double? = null
    private var longitude: Double? = null

    fun fetchListOfVenues() {
        viewModelScope.launch {
            viewState.postValue(ListOfVenuesViewState.Loading)

            when(val result = repo.fetchListOfVenues(latitude.toString(), longitude.toString())) {
                is Success -> {
                    viewState.postValue(ListOfVenuesViewState.Success(result.data.data!!.venues))
                }
                is Failure -> {
                    viewState.postValue(ListOfVenuesViewState.Error(result.error ?: ""))
                }
            }
        }
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }
}