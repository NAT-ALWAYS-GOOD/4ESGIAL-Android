package com.nat.cineandroid.ui

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedLocationViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val _userLocation = MutableLiveData<Location?>()
    val userLocation: LiveData<Location?> get() = _userLocation

    private val _selectedTheater = MutableLiveData<TheaterEntity>()
    val selectedTheater: LiveData<TheaterEntity> get() = _selectedTheater

    init {
        requestUserLocation()
    }

    @SuppressLint("MissingPermission")
    fun requestUserLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            _userLocation.value = location
        }.addOnFailureListener {
            _userLocation.value = null
        }
    }

    fun selectClosestTheater(theaters: List<TheaterEntity>) {
        val location = _userLocation.value
        val closestTheater = location?.let { loc ->
            theaters.minByOrNull { theater ->
                val theaterLoc = Location("").apply {
                    latitude = theater.latitude
                    longitude = theater.longitude
                }
                loc.distanceTo(theaterLoc)
            }
        }
        _selectedTheater.value = closestTheater ?: theaters.firstOrNull()
    }

    fun updateSelectedTheater(theater: TheaterEntity) {
        _selectedTheater.value = theater
    }
}