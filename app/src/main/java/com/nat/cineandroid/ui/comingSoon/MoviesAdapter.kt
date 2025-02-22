package com.nat.cineandroid.ui.comingSoon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nat.cineandroid.data.movie.entity.MovieEntity
import com.nat.cineandroid.databinding.UnreleasedMovieTileBinding
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MoviesAdapter : ListAdapter<MovieEntity, MoviesAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean =
                oldItem == newItem
        }
    }

    inner class MovieViewHolder(private val binding: UnreleasedMovieTileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.movieTile.movieTitle.text = movie.title

            Glide.with(binding.root.context)
                .load(movie.imageUrl)
                .into(binding.movieTile.moviePosterImage)

            binding.releaseDateIndicator.releaseDate.text = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .withZone(ZoneId.systemDefault())
                .format(movie.releaseDate)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = UnreleasedMovieTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
