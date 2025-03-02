package com.nat.cineandroid.ui.user.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nat.cineandroid.data.session.entity.ReservationItemData
import com.nat.cineandroid.databinding.ItemReservationBinding
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ReservationAdapter(
    private val onReservationClick: (ReservationItemData) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    private var reservations: List<ReservationItemData> = emptyList()

    fun submitList(list: List<ReservationItemData>) {
        reservations = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReservationBinding.inflate(inflater, parent, false)
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val item = reservations[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onReservationClick(item)
        }
    }

    override fun getItemCount(): Int = reservations.size

    class ReservationViewHolder(val binding: ItemReservationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReservationItemData) {
            binding.theaterName.text = item.theater.name
            binding.roomName.text = item.cinemaRoom.name
            binding.reservationNumber.text = itemView.context.getString(
                com.nat.cineandroid.R.string.reservation_reference,
                item.reservation.reference
            )
            binding.movieName.text = item.movie.title

            val dateFormatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())
            val hoursFormatter =
                DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
            binding.reservationDate.text = "${dateFormatter.format(item.session.startTime)}"
            val start = hoursFormatter.format(item.session.startTime)
            val end = hoursFormatter.format(item.session.endTime)
            binding.sessionTime.text = itemView.context.getString(
                com.nat.cineandroid.R.string.session_hours,
                start,
                end
            )
        }
    }
}