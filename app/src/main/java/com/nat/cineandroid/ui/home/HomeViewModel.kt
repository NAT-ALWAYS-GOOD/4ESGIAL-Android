package com.nat.cineandroid.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.data.movie.entity.MovieWithSessions
import com.nat.cineandroid.data.movie.repository.MovieRepository
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import com.nat.cineandroid.data.theater.repository.TheaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val theaterRepository: TheaterRepository
) : ViewModel() {

    private val _moviesWithSessions = MutableLiveData<List<MovieWithSessions>>()
    val moviesWithSessions: LiveData<List<MovieWithSessions>> get() = _moviesWithSessions

    private val _theaters = MutableLiveData<List<TheaterEntity>>()
    val theaters: LiveData<List<TheaterEntity>> get() = _theaters

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchMoviesWithSessions(theaterId: Int) {
        _errorMessage.value = null

        viewModelScope.launch {
            when (val result = movieRepository.getMoviesByTheater(theaterId)) {
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
        }
    }

    fun fetchTheaters() {
        _errorMessage.value = null

        viewModelScope.launch {
            Log.d("HomeViewModel", "Fetching theaters")
            when (val result = theaterRepository.getTheaters()) {
                is HttpResult.Success -> {
                    _theaters.postValue(result.data)
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
        }
    }
}