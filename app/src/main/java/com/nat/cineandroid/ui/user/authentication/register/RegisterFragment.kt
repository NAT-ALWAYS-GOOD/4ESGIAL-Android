package com.nat.cineandroid.ui.user.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nat.cineandroid.databinding.FragmentRegisterBinding
import com.nat.cineandroid.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        hideNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RegisterState.Success -> (activity as MainActivity).navigateToHome()

                is RegisterState.Error -> Toast.makeText(
                    requireContext(),
                    state.message,
                    Toast.LENGTH_LONG
                ).show()

                RegisterState.Loading -> Toast.makeText(
                    requireContext(),
                    "Loading...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                viewModel.performRegister(username, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.loginLink.setOnClickListener {
            (activity as MainActivity).navigateToLogin()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideNavigationBar() {
        val window = requireActivity().window
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }
}