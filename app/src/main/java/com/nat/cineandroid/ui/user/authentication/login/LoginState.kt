package com.nat.cineandroid.ui.user.authentication.login


sealed class LoginState {
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}