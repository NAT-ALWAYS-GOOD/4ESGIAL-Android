package com.nat.cineandroid.ui.user

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.nat.cineandroid.R
import com.nat.cineandroid.data.model.UserEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        userNameTextView = findViewById(R.id.userNameTextView)
        userEmailTextView = findViewById(R.id.userEmailTextView)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.user.observe(this) { user ->
            user?.let { displayUserData(it.user) }
        }

        viewModel.fetchUser(1)
    }

    private fun displayUserData(user: UserEntity) {
        userNameTextView.text = user.username
        userEmailTextView.text = user.password
    }
}