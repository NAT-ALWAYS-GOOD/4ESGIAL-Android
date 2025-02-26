package com.nat.cineandroid.ui.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.data.movie.entity.MovieEntity
import com.nat.cineandroid.data.movie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movie = MutableLiveData<MovieEntity>()
    val movie: MutableLiveData<MovieEntity> get() = _movie

    fun fetchMovieById(movieId: Int) {
        viewModelScope.launch {
            val movie = movieRepository.getMovieById(movieId)
            _movie.postValue(movie)
        }
    }
}