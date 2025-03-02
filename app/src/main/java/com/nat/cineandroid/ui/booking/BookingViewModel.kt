package com.nat.cineandroid.ui.booking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nat.cineandroid.core.api.HttpResult
import com.nat.cineandroid.data.session.entity.SessionWithSeats
import com.nat.cineandroid.data.session.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _sessions = MutableLiveData<List<SessionWithSeats>>()
    val sessions: LiveData<List<SessionWithSeats>> get() = _sessions

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = _selectedDate

    private val _selectedTime = MutableLiveData<String>()
    val selectedTime: LiveData<String> get() = _selectedTime

    private val _selectedSession = MutableLiveData<SessionWithSeats>()
    val selectedSession: LiveData<SessionWithSeats> get() = _selectedSession

    private val _availableTimes = MutableLiveData<List<String>>()
    val availableTimes: LiveData<List<String>> get() = _availableTimes

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadSessions(movieId: Int, theaterId: Int) {
        viewModelScope.launch {
            when (val result =
                sessionRepository.getAllSessionsFromMovieIdAndTheaterId(movieId, theaterId)) {
                is HttpResult.Success -> {
                    _sessions.postValue(result.data)
                    result.data.minByOrNull { it.session.startTime }?.let { session ->
                        val defaultDate = extractDate(session.session.startTime)
                        _selectedDate.value = defaultDate
                        Log.d("BookingViewModel", "Default date: $defaultDate")

                        val timesForDay = result.data
                            .filter { extractDate(it.session.startTime) == defaultDate }
                            .sortedBy { it.session.startTime }
                            .map { extractTime(it.session.startTime) }
                            .distinct()
                        Log.d("BookingViewModel", "Times for day: $timesForDay")
                        _availableTimes.value = timesForDay

                        val defaultTime = extractTime(session.session.startTime)
                        _selectedTime.value = defaultTime
                        Log.d("BookingViewModel", "Default time: $defaultTime")
                        _selectedSession.value =
                            result.data.first { extractTime(it.session.startTime) == defaultTime }
                    }
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

    fun onDateSelected(date: String) {
        _selectedDate.value = date
        val sessionsForDate =
            _sessions.value?.filter { extractDate(it.session.startTime) == date } ?: emptyList()
        if (sessionsForDate.isNotEmpty()) {
            val defaultTime =
                extractTime(sessionsForDate.minByOrNull { it.session.startTime }!!.session.startTime)
            _selectedTime.value = defaultTime
            _selectedSession.value =
                sessionsForDate.first { extractTime(it.session.startTime) == defaultTime }

            val timesForDate = sessionsForDate
                .map { extractTime(it.session.startTime) }
                .distinct()
                .sorted()
            _availableTimes.value = timesForDate
        } else {
            _availableTimes.value = emptyList()
        }
    }

    fun onTimeSelected(time: String) {
        _selectedTime.value = time
        val date = _selectedDate.value
        if (date != null) {
            _selectedSession.value = _sessions.value?.firstOrNull {
                extractDate(it.session.startTime) == date &&
                        extractTime(it.session.startTime) == time
            }
        }
    }

    fun extractDate(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM dd", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant).replaceFirstChar { it.uppercaseChar() }
    }

    fun extractTime(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }
}
