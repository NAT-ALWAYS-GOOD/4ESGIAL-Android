package com.nat.cineandroid.ui.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.data.user.entity.UserEntity
import com.nat.cineandroid.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _user = MutableLiveData<UserEntity?>()
    val user get() = _user

    private val _successCode = MutableLiveData<Int?>()
    val successCode get() = _successCode

    private val _errorCode = MutableLiveData<Int?>()
    val errorCode get() = _errorCode

    fun fetchUser(userId: Int) {
        viewModelScope.launch {
            _user.postValue(userRepository.getUser(userId))
        }
    }

    fun updateFavoriteTheater(userId: Int, theaterId: Int) {
        viewModelScope.launch {
            when (val result = userRepository.updateFavoriteTheater(userId, theaterId)) {
                is HttpResult.Success -> {
                    fetchUser(userId)
                }

                is HttpResult.HttpError -> {
                    _errorCode.postValue(result.code)
                }

                is HttpResult.NetworkError -> {
                    _errorCode.postValue(0)
                }

                is HttpResult.NoData -> {
                    _errorCode.postValue(1)
                }
            }
        }
    }
}