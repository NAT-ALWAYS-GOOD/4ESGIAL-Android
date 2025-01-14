package com.nat.cineandroid.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.nat.cineandroid.databinding.FragmentLoginBinding
import com.nat.cineandroid.data.user.repository.UserRepository
import com.nat.cineandroid.core.api.HttpResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    // Injection de UserRepository avec Hilt
    @Inject lateinit var userRepository: UserRepository

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        hideNavigationBar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val email = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                performLogin(email, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLogin(email: String, password: String) {
        lifecycleScope.launch {
            when (val result = userRepository.login(email, password)) {
                is HttpResult.Success -> {
                    Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_LONG).show()
                    (activity as MainActivity).navigateBack()
                }
                is HttpResult.HttpError -> {
                    Toast.makeText(requireContext(), "HttpError" + result.message, Toast.LENGTH_LONG).show()
                }
                is HttpResult.NetworkError -> {
                    Log.e("LoginFragment", "Network error" + result.message)
                    Toast.makeText(requireContext(), "Network" + result.message, Toast.LENGTH_LONG).show()
                }
                is HttpResult.NoData -> {
                    Toast.makeText(requireContext(), "No data returned", Toast.LENGTH_LONG).show()
                }
            }
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