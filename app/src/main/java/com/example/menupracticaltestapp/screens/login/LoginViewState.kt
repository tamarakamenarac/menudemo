package com.example.menupracticaltestapp.screens.login

import com.example.menupracticaltestapp.data.LoginResponse

sealed class LoginViewState {
    data class Success(val loginResponse: LoginResponse): LoginViewState()
    data class Error(val error: String): LoginViewState()
    object Loading: LoginViewState()
}