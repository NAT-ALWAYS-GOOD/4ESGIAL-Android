package com.nat.cineandroid.ui.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.nat.cineandroid.databinding.DialogQrcodeBinding

class QRCodeDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_QR_CODE = "arg_qr_code"

        fun newInstance(qrCode: String): QRCodeDialogFragment {
            val fragment = QRCodeDialogFragment()
            val args = Bundle()
            args.putString(ARG_QR_CODE, qrCode)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: DialogQrcodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, com.nat.cineandroid.R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogQrcodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val qrCodeString = arguments?.getString(ARG_QR_CODE)

        Glide.with(this)
            .load(qrCodeString)
            .into(binding.qrCodeImageView)

        binding.root.setOnClickListener {
            dismiss()
        }
    }
}