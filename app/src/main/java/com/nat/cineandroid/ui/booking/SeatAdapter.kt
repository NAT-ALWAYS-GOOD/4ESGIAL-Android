package com.nat.cineandroid.ui.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nat.cineandroid.data.session.entity.SeatEntity
import com.nat.cineandroid.databinding.ItemGapBinding
import com.nat.cineandroid.databinding.ItemSeatBinding

//class SeatAdapter(
//    private val onItemClicked: (SeatEntity) -> Unit
//) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {
//
//    private var seats: List<SeatEntity> = emptyList()
//    private var selectedSeatNumbers = mutableSetOf<Int>()
//
//    fun submitList(newSeats: List<SeatEntity>) {
//        seats = newSeats
//        notifyDataSetChanged()
//    }
//
//    fun getSelectedSeats(): List<SeatEntity> {
//        return seats.filter { selectedSeatNumbers.contains(it.seatNumber) }
//    }
//
//    private fun toggleSeatSelection(seatNumber: Int) {
//        if (selectedSeatNumbers.contains(seatNumber)) {
//            selectedSeatNumbers.remove(seatNumber)
//        } else {
//            selectedSeatNumbers.add(seatNumber)
//        }
//        notifyDataSetChanged()
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
//        val binding = ItemSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return SeatViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
//        val seat = seats[position]
//        holder.bind(seat, selectedSeatNumbers, onItemClicked) { seatNumber ->
//            // Ne permet de changer la sélection que si le siège n'est pas réservé
//            if (!seat.isReserved) {
//                toggleSeatSelection(seatNumber)
//            }
//        }
//    }
//
//    override fun getItemCount(): Int = seats.size
//
//    class SeatViewHolder(private val binding: ItemSeatBinding) : RecyclerView.ViewHolder(binding.root) {
//        /**
//         * @param selectedSeatNumbers Ensemble des numéros de sièges actuellement sélectionnés.
//         * @param onItemClicked Callback appelé lorsqu'un siège est cliqué.
//         * @param toggleSelection Callback pour basculer la sélection d'un siège.
//         */
//        fun bind(
//            seat: SeatEntity,
//            selectedSeatNumbers: Set<Int>,
//            onItemClicked: (SeatEntity) -> Unit,
//            toggleSelection: (Int) -> Unit
//        ) {
//            binding.seatLabel.text = seat.seatNumber.toString()
//            val context = binding.root.context
//            // Détermine la couleur de fond :
//            // - Rouge si le siège est réservé.
//            // - Bleu si le siège est sélectionné.
//            // - Vert sinon (disponible).
//            val bgColor = when {
//                seat.isReserved -> context.getColor(android.R.color.holo_red_dark)
//                selectedSeatNumbers.contains(seat.seatNumber) -> context.getColor(android.R.color.holo_blue_dark)
//                else -> context.getColor(android.R.color.holo_green_light)
//            }
//            binding.seatLabel.setBackgroundColor(bgColor)
//
//            binding.root.setOnClickListener {
//                // On déclenche la bascule de la sélection
//                toggleSelection(seat.seatNumber)
//                onItemClicked(seat)
//            }
//        }
//    }
//}

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
            binding.seatLabel.text = seat.seatNumber.toString()
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