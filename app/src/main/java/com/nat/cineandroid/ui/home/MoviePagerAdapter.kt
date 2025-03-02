package com.nat.cineandroid.ui.home

import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nat.cineandroid.data.movie.entity.MovieWithSessions
import com.nat.cineandroid.databinding.MovieTileBinding
import com.nat.cineandroid.ui.home.MoviePagerAdapter.MoviesViewHolder

class MoviePagerAdapter(
    private val onMovieClick: (MovieWithSessions) -> Unit
) : RecyclerView.Adapter<MoviesViewHolder>() {

    private val movies = mutableListOf<MovieWithSessions>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieTileBinding.inflate(inflater, parent, false)
        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movieWithSessions = movies[position]
        holder.bind(movieWithSessions, onMovieClick)
    }

    override fun getItemCount(): Int = movies.size

    fun submitList(list: List<MovieWithSessions>) {
        movies.clear()
        movies.addAll(list)
        notifyDataSetChanged()
    }

    class MoviesViewHolder(
        private val binding: MovieTileBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieWithSessions: MovieWithSessions, onClick: (MovieWithSessions) -> Unit) {
            val movie = movieWithSessions.movie
            binding.movieTitle.text = movie.title
            Log.d("MovieAdapter", "Binding movie: ${movie.title}")
            Log.d("MovieAdapter", "Binding movie: ${movie.imageUrl}")

            val newWidthDp = 300
            val newHeightDp = 500

            val scale = binding.root.context.resources.displayMetrics.density
            val newWidthPx = (newWidthDp * scale).toInt()
            val newHeightPx = (newHeightDp * scale).toInt()

            val params = binding.moviePosterImage.layoutParams
            params.width = newWidthPx
            params.height = newHeightPx
            binding.moviePosterImage.layoutParams = params

            binding.movieTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32f)

            Glide.with(binding.root.context)
                .load(movie.imageUrl)
                .into(binding.moviePosterImage)

            binding.moviePoster.setOnClickListener {
                onClick(movieWithSessions)
            }
        }
    }
}