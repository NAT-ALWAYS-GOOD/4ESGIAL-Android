package com.nat.cineandroid.ui.user.authentication.register

sealed class RegisterState {
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}