package com.nat.cineandroid.ui.user.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState> get() = _state

    fun performLogin(username: String, password: String) {
        viewModelScope.launch {
            when (val result = userRepository.login(username, password)) {
                is HttpResult.Success -> _state.value = LoginState.Success
                is HttpResult.HttpError -> {
                    if (result.code == 404) {
                        _state.value = LoginState.Error("Invalid username or password")
                    } else {
                        _state.value = LoginState.Error(result.message)
                    }
                }
                is HttpResult.NetworkError -> _state.value =
                    LoginState.Error("Network error: ${result.message}")

                is HttpResult.NoData -> _state.value = LoginState.Error("No data returned")
            }
        }
    }
}