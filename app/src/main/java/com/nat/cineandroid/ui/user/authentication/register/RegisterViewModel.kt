package com.nat.cineandroid.ui.user.authentication.register

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
class RegisterViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _state = MutableLiveData<RegisterState>()
    val state: LiveData<RegisterState> get() = _state

    fun performRegister(username: String, password: String) {
        viewModelScope.launch {
            when (val result = userRepository.register(username, password)) {
                is HttpResult.Success -> _state.value = RegisterState.Success
                is HttpResult.HttpError -> _state.value = if (result.code == 409) {
                    RegisterState.Error("Username already exists")
                } else {
                    RegisterState.Error(result.message)
                }

                is HttpResult.NetworkError -> _state.value =
                    RegisterState.Error("Network error: ${result.message}")

                is HttpResult.NoData -> _state.value = RegisterState.Error("No data returned")
            }
        }
    }
}