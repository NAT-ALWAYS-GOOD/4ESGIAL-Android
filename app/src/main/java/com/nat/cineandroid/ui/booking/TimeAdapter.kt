package com.nat.cineandroid.ui.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nat.cineandroid.databinding.ItemTimeBinding

class TimeAdapter(private val onItemClicked: (String) -> Unit) :
    RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    private var times: List<String> = emptyList()
    private var selectedTime: String? = null

    fun submitList(list: List<String>) {
        times = list
        notifyDataSetChanged()
    }

    fun setSelectedTime(time: String) {
        selectedTime = time
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding = ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val time = times[position]
        val isSelected = time == selectedTime
        holder.bind(time, isSelected, onItemClicked)
    }

    override fun getItemCount(): Int = times.size

    class TimeViewHolder(private val binding: ItemTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(time: String, isSelected: Boolean, onItemClicked: (String) -> Unit) {
            binding.timeLabel.text = time

            binding.root.isSelected = isSelected

            binding.root.setOnClickListener {
                onItemClicked(time)
            }
        }
    }
}
