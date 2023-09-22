package com.example.menupracticaltestapp.screens.login

import android.content.SharedPreferences.Editor
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menupracticaltestapp.helpers.Failure
import com.example.menupracticaltestapp.helpers.PREFERENCES_ACCESS_TOKEN
import com.example.menupracticaltestapp.helpers.Success
import com.example.menupracticaltestapp.repo.RemoteRepo
import kotlinx.coroutines.launch

class LoginViewModel (
    private val repo: RemoteRepo,
    private val sharedPreferencesEditor: Editor
) : ViewModel() {

    val viewState: MutableLiveData<LoginViewState> = MutableLiveData()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            viewState.postValue(LoginViewState.Loading)

            when(val result = repo.login(email, password)) {
                is Success -> {
                    sharedPreferencesEditor.putString(PREFERENCES_ACCESS_TOKEN, result.data.data?.token?.value).commit()
                    viewState.postValue(LoginViewState.Success(result.data))
                }
                is Failure -> {
                    viewState.postValue(LoginViewState.Error(result.error ?: ""))
                }
            }
        }
    }
}