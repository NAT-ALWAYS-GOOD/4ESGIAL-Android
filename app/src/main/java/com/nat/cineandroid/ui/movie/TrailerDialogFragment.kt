package com.nat.cineandroid.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.nat.cineandroid.R
import com.nat.cineandroid.databinding.DialogTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class TrailerDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_YOUTUBE_ID = "arg_youtube_id"

        fun newInstance(youtubeId: String): TrailerDialogFragment {
            val fragment = TrailerDialogFragment()
            val args = Bundle()
            args.putString(ARG_YOUTUBE_ID, youtubeId)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: DialogTrailerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogTrailerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val youtubeId = arguments?.getString(ARG_YOUTUBE_ID)
        if (youtubeId.isNullOrEmpty()) {
            dismiss()
            return
        }


        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(youtubeId, 0f)
            }
        })

        binding.root.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}