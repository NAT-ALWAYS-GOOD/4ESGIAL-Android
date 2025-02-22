package com.nat.cineandroid.ui.comingSoon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.data.movie.entity.MovieEntity
import com.nat.cineandroid.data.movie.repository.MovieRepository
import com.nat.cineandroid.data.theater.repository.TheaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {
    private val _unreleasedMovies = MutableLiveData<List<MovieEntity>>()
    val unreleasedMovies get() = _unreleasedMovies

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchUnreleasedMovies() {
        viewModelScope.launch {
            when (val result = movieRepository.getNotReleasedMovies()) {
                is HttpResult.Success -> {
                    _unreleasedMovies.postValue(result.data)
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