package com.nat.cineandroid.ui.booking

import com.nat.cineandroid.data.session.entity.SeatEntity

sealed class SeatItem {
    data class SeatData(val seat: SeatEntity) : SeatItem()
    object Gap : SeatItem()
}