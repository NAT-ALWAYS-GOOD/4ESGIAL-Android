package com.nat.cineandroid.ui.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nat.cineandroid.databinding.ItemDateBinding

class DateAdapter(private val onItemClicked: (String) -> Unit) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    private var dates: List<String> = emptyList()
    private var selectedDate: String? = null

    fun submitList(newDates: List<String>) {
        dates = newDates
        notifyDataSetChanged()
    }

    fun setSelectedDate(date: String) {
        selectedDate = date
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates[position]
        val isSelected = date == selectedDate
        holder.bind(date, isSelected, onItemClicked)
    }

    override fun getItemCount(): Int = dates.size

    class DateViewHolder(private val binding: ItemDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(date: String, isSelected: Boolean, onItemClicked: (String) -> Unit) {
            binding.dateLabel.text = date
            // Changer la couleur de fond en fonction de l'état de sélection
            binding.root.isSelected = isSelected

            binding.root.setOnClickListener { onItemClicked(date) }
        }
    }
}