package com.nat.cineandroid.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.data.model.UserEntity
import com.nat.cineandroid.data.model.UserWithReservations
import com.nat.cineandroid.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

/*    // LiveData pour l'observation des données utilisateur
    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user

    // LiveData pour les erreurs
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                // Récupère les données utilisateur depuis le repository
                _user.value = userRepository.getUser()
            } catch (e: Exception) {
                _error.value = "Erreur de récupération des données : ${e.message}"
            }
        }
    }*/

    private val _user = MutableLiveData<UserWithReservations?>()
    val user: LiveData<UserWithReservations?> = _user

    fun fetchUser(userId: Int) {
        viewModelScope.launch {
            val userFromRepo = userRepository.getUser(userId)
            _user.postValue(userFromRepo)
        }
    }
}