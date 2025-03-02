package com.nat.cineandroid.ui.user.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.data.session.entity.FullReservationsData
import com.nat.cineandroid.data.session.entity.ReservationItemData
import com.nat.cineandroid.data.session.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _reservations = MutableLiveData<FullReservationsData>()
    val reservations: LiveData<FullReservationsData> get() = _reservations

    private val now = Instant.now()

    val upcomingReservations: LiveData<List<ReservationItemData>> = _reservations.map { data ->
        mapFullDataToReservationItems(data).filter { it.session.startTime.isAfter(now) }
    }

    val pastReservations: LiveData<List<ReservationItemData>> = _reservations.map { data ->
        mapFullDataToReservationItems(data).filter { it.session.endTime.isBefore(now) }
    }

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchReservations(userId: Int) {
        _errorMessage.value = null

        viewModelScope.launch {
            when (val result = sessionRepository.getUserReservations(userId)) {
                is HttpResult.Success -> {
                    _reservations.postValue(result.data)
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

    private fun mapFullDataToReservationItems(data: FullReservationsData): List<ReservationItemData> {
        return data.reservations.map { reservation ->
            val session = data.sessions.find { it.id == reservation.sessionId }!!
            val movie = data.movies.find { it.id == session.movieId }!!
            val cinemaRoom = data.cinemaRooms.find { it.id == session.roomId }!!
            val theater = data.theaters.find { it.id == cinemaRoom.theaterId }!!
            ReservationItemData(reservation, session, movie, cinemaRoom, theater)
        }
    }
}