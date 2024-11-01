package com.nat.cineandroid.ui.user

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nat.cineandroid.R
import com.nat.cineandroid.data.model.UserEntity
import com.nat.cineandroid.data.utils.ApiResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        userNameTextView = findViewById(R.id.userNameTextView)
        userEmailTextView = findViewById(R.id.userEmailTextView)

        viewModel.user.observe(this) { result ->
            when (result) {
                is ApiResult.Success -> result.data?.let { displayUserData(it.user) }
                is ApiResult.HttpError -> showError("Erreur HTTP : ${result.code} ${result.message}")
                is ApiResult.NetworkError -> showError("Erreur Réseau : ${result.message}")
                is ApiResult.NoData -> showError("Erreur : ${result.message}")
            }
        }

        viewModel.fetchUser(1)
    }

    private fun displayUserData(user: UserEntity) {
        userNameTextView.text = user.username
        userEmailTextView.text = user.password
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}