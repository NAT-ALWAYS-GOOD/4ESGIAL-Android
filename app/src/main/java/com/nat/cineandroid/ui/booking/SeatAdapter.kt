package com.nat.cineandroid.ui.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nat.cineandroid.data.session.entity.SeatEntity
import com.nat.cineandroid.databinding.ItemGapBinding
import com.nat.cineandroid.databinding.ItemSeatBinding

private const val TYPE_SEAT = 0
private const val TYPE_GAP = 1

class SeatAdapter(
    private val onItemClicked: (SeatEntity) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<SeatItem> = emptyList()
    // Pour gérer la sélection multiple, on utilisera un MutableSet de numéros de sièges sélectionnés.
    private val selectedSeatNumbers = mutableSetOf<Int>()

    fun submitList(newItems: List<SeatItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun toggleSeatSelection(seatNumber: Int) {
        if (selectedSeatNumbers.contains(seatNumber)) {
            selectedSeatNumbers.remove(seatNumber)
        } else {
            selectedSeatNumbers.add(seatNumber)
        }
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): SeatItem = items[position]

    fun getSelectedSeats(): List<SeatEntity> {
        return items.filterIsInstance<SeatItem.SeatData>()
            .map { it.seat }
            .filter { selectedSeatNumbers.contains(it.seatNumber) }
    }

    fun resetSelection() {
        selectedSeatNumbers.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SeatItem.SeatData -> TYPE_SEAT
            is SeatItem.Gap -> TYPE_GAP
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SEAT -> {
                val binding = ItemSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SeatViewHolder(binding)
            }
            TYPE_GAP -> {
                val binding = ItemGapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GapViewHolder(binding)
            }
            else -> throw IllegalArgumentException("ViewType inconnu")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is SeatItem.SeatData -> (holder as SeatViewHolder).bind(item.seat, selectedSeatNumbers, onItemClicked) {
                toggleSeatSelection(it)
            }
            is SeatItem.Gap -> { /* Rien à binder pour un gap */ }
        }
    }

    override fun getItemCount(): Int = items.size

    class SeatViewHolder(private val binding: ItemSeatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            seat: SeatEntity,
            selectedSeatNumbers: Set<Int>,
            onItemClicked: (SeatEntity) -> Unit,
            toggleSelection: (Int) -> Unit
        ) {
            binding.seatLabel.text = itemView.context.getString(com.nat.cineandroid.R.string.seat_number, seat.seatNumber)
            val context = binding.root.context
            val bgColor = when {
                seat.isReserved -> context.getColor(android.R.color.holo_red_dark)
                selectedSeatNumbers.contains(seat.seatNumber) -> context.getColor(android.R.color.holo_blue_dark)
                else -> context.getColor(android.R.color.holo_green_light)
            }
            binding.seatLabel.setBackgroundColor(bgColor)
            binding.root.setOnClickListener {
                if (!seat.isReserved) {
                    toggleSelection(seat.seatNumber)
                    onItemClicked(seat)
                }
            }
        }
    }

    class GapViewHolder(binding: ItemGapBinding) : RecyclerView.ViewHolder(binding.root)
}