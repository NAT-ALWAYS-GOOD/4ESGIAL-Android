package com.nat.cineandroid.data.user

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nat.cineandroid.core.cache.ApplicationCache
import com.nat.cineandroid.data.user.entity.UserEntity
import com.nat.cineandroid.data.user.dao.UserDAO
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class UserDAOTest {
    private lateinit var database: ApplicationCache
    private lateinit var userDAO: UserDAO

    @Before
    fun setUp() = runBlocking {
        // Création d'une base de données en mémoire
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ApplicationCache::class.java
        )
            .allowMainThreadQueries()
            .build()

        userDAO = database.userDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insert_and_getUser_should_work() = runTest {
        val user = UserEntity(
            id = 1,
            username = "Test User",
            isActive = true,
            favoriteTheaterId = null
        )
        userDAO.insert(user)
        val retrieved = userDAO.getUser(1)
        assertThat(retrieved).isNotNull
        assertThat(retrieved.id).isEqualTo(1)
        assertThat(retrieved.username).isEqualTo("Test User")
    }

    @Test
    fun updateUser_should_update_correctly() = runTest {
        val user = UserEntity(
            id = 1,
            username = "Test User",
            isActive = true,
            favoriteTheaterId = null
        )
        userDAO.insert(user)
        val updatedUser = user.copy(username = "Updated User")
        userDAO.updateUser(updatedUser)
        val retrieved = userDAO.getUser(1)
        assertThat(retrieved).isNotNull
        assertThat(retrieved.id).isEqualTo(1)
        assertThat(retrieved.username).isEqualTo("Updated User")
    }
}