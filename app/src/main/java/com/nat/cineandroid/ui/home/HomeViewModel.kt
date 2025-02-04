package com.nat.cineandroid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.data.movie.entity.MovieWithSessions
import com.nat.cineandroid.data.movie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _moviesWithSessions = MutableLiveData<List<MovieWithSessions>>()
    val moviesWithSessions: LiveData<List<MovieWithSessions>> get() = _moviesWithSessions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchMoviesWithSessions(theaterId: Int) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            when (val result = repository.getMoviesByTheater(theaterId)) {
                is HttpResult.Success -> {
                    _moviesWithSessions.postValue(result.data)
                }

                is HttpResult.HttpError -> {
                    _errorMessage.postValue("Server Error (${result.code}): ${result.message}")
                }

                is HttpResult.NetworkError -> {
                    _errorMessage.postValue("Network Error: ${result.message}")
                }

                is HttpResult.NoData -> {
                    _errorMessage.postValue("No Data: ${result.message}")
                }
            }
            _isLoading.postValue(false)
        }
    }
}