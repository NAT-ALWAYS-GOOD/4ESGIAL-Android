package com.nat.cineandroid.ui.maps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import com.nat.cineandroid.data.user.entity.UserEntity
import com.nat.cineandroid.databinding.FragmentTheaterSelectionBottomSheetListDialogItemBinding

class TheaterAdapter(
    private val onItemClicked: (TheaterEntity) -> Unit,
    private val onFavoriteClick: (TheaterEntity) -> Unit
) :
    RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder>() {

    private var theaters: List<TheaterEntity> = emptyList()
    private var selectedTheaterId: Int? = null
    private var currentUser: UserEntity? = null

    fun setUser(user: UserEntity?) {
        currentUser = user
        notifyDataSetChanged()
    }


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
        holder.bind(theater, onItemClicked, isSelected, currentUser, onFavoriteClick)
        holder.itemView.setOnClickListener {
            setSelectedTheater(theater.id)
            onItemClicked(theater)
        }
    }

    override fun getItemCount(): Int = theaters.size

    class TheaterViewHolder(private val binding: FragmentTheaterSelectionBottomSheetListDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            theater: TheaterEntity,
            onItemClicked: (TheaterEntity) -> Unit,
            isSelected: Boolean,
            user: UserEntity?,
            onFavoriteClick: (TheaterEntity) -> Unit
        ) {
            binding.textViewTheaterName.text = theater.name

            val selectedColor = binding.root.context.getColor(android.R.color.holo_red_dark)
            val defaultColor = binding.root.context.getColor(com.nat.cineandroid.R.color.red)
            binding.root.setCardBackgroundColor(if (isSelected) selectedColor else defaultColor)

            binding.root.setOnClickListener { onItemClicked(theater) }

            if (user == null) {
                binding.imageViewFavorite.visibility = View.GONE
            } else {
                binding.imageViewFavorite.visibility = View.VISIBLE
                if (user.favoriteTheaterId == theater.id) {
                    binding.imageViewFavorite.setImageResource(com.nat.cineandroid.R.drawable.heart_full)
                } else {
                    binding.imageViewFavorite.setImageResource(com.nat.cineandroid.R.drawable.heart_empty)
                }
                binding.imageViewFavorite.setOnClickListener {
                    if (user.favoriteTheaterId != theater.id) {
                        onFavoriteClick(theater)
                    }
                }
            }
        }
    }
}