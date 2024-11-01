package com.nat.cineandroid.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.data.model.UserEntity
import com.nat.cineandroid.data.model.UserWithReservations
import com.nat.cineandroid.data.repository.UserRepository
import com.nat.cineandroid.data.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _user = MutableLiveData<ApiResult<UserWithReservations?>>()
    val user: LiveData<ApiResult<UserWithReservations?>> = _user

    fun fetchUser(userId: Int) {
        viewModelScope.launch {
            val result = userRepository.getUser(userId)
            _user.postValue(result)
        }
    }
}