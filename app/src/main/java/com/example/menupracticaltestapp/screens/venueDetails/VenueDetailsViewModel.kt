package com.example.menupracticaltestapp.screens.venueDetails

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menupracticaltestapp.helpers.PREFERENCES_ACCESS_TOKEN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VenueDetailsViewModel(
    private val sharedPreferencesEditor: SharedPreferences.Editor
) : ViewModel() {

    val viewState = MutableLiveData<VenueDetailsState>()

    fun logout() {
        viewModelScope.launch {
            sharedPreferencesEditor.remove(PREFERENCES_ACCESS_TOKEN).commit()
            viewState.postValue(VenueDetailsState.Logout)
        }
    }
}