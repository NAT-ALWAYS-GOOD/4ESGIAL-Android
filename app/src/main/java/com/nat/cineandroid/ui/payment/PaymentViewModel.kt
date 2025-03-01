package com.nat.cineandroid.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.data.movie.repository.MovieRepository
import com.nat.cineandroid.data.session.repository.SessionRepository
import com.nat.cineandroid.data.theater.repository.TheaterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val theaterRepository: TheaterRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<PaymentUiState>()
    val uiState: LiveData<PaymentUiState> get() = _uiState

    fun loadPaymentData(movieId: Int, theaterId: Int, sessionId: Int, seatNumbers: List<Int>) {
        viewModelScope.launch {
            val session = sessionRepository.getSessionFromCache(sessionId)
            val movie = movieRepository.getMovieById(movieId)
            val theater = theaterRepository.getTheaterFromCache(theaterId)

            val totalPrice = seatNumbers.size * 10.0

            val state = PaymentUiState(
                movieTitle = movie.title,
                moviePosterUrl = movie.imageUrl,
                sessionStartTime = session.startTime,
                theaterName = theater.name,
                seatNumbers = seatNumbers,
                totalPrice = totalPrice,
            )
            _uiState.postValue(state)
        }
    }

    fun extractDate(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant).replaceFirstChar { it.uppercaseChar() }
    }

    fun extractTime(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }
}