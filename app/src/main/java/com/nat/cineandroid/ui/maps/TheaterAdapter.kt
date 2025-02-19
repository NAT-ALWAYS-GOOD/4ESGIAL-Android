package com.nat.cineandroid.ui.maps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import com.nat.cineandroid.databinding.FragmentTheaterSelectionBottomSheetListDialogItemBinding

class TheaterAdapter(private val onItemClicked: (TheaterEntity) -> Unit) :
    RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder>() {

    private var theaters: List<TheaterEntity> = emptyList()
    private var selectedTheaterId: Int? = null

    fun getSelectedTheater(): TheaterEntity? {
        return theaters.find { it.id == selectedTheaterId }
    }

    fun getSelectedIndex(): Int {
        return theaters.indexOfFirst { it.id == selectedTheaterId }
            .let { if (it >= 0) it else 0 }
    }

    fun setSelectedTheater(theaterId: Int) {
        selectedTheaterId = theaterId
        notifyDataSetChanged()
    }

    fun submitList(list: List<TheaterEntity>) {
        theaters = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheaterViewHolder {
        val binding = FragmentTheaterSelectionBottomSheetListDialogItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TheaterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TheaterViewHolder, position: Int) {
        val theater = theaters[position]
        val isSelected = theater.id == selectedTheaterId
        holder.bind(theater, onItemClicked, isSelected)
        holder.itemView.setOnClickListener {
            setSelectedTheater(theater.id)
            onItemClicked(theater)
        }
    }

    override fun getItemCount(): Int = theaters.size

    class TheaterViewHolder(private val binding: FragmentTheaterSelectionBottomSheetListDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(theater: TheaterEntity, onItemClicked: (TheaterEntity) -> Unit, isSelected: Boolean) {
            binding.textViewTheaterName.text = theater.name

            val selectedColor = binding.root.context.getColor(com.nat.cineandroid.R.color.red20)
            val defaultColor = binding.root.context.getColor(com.nat.cineandroid.R.color.red)
            binding.root.setBackgroundColor(if (isSelected) selectedColor else defaultColor)

            binding.root.setOnClickListener { onItemClicked(theater) }
        }
    }
}