package com.nat.cineandroid.data.theater

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nat.cineandroid.core.cache.ApplicationCache
import com.nat.cineandroid.data.theater.dao.TheaterDAO
import com.nat.cineandroid.data.theater.entity.TheaterEntity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TheaterDAOTest {
    private lateinit var database: ApplicationCache
    private lateinit var theaterDAO: TheaterDAO

    @Before
    fun setUp() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ApplicationCache::class.java
        )
            .allowMainThreadQueries()
            .build()

        theaterDAO = database.theaterDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertTheaters_and_getTheaters_should_work() = runTest {
        val theaters = listOf(
            TheaterEntity(
                id = 1,
                name = "Theater One",
                description = "Description One",
                streetName = "Main Street",
                streetNumber = "1",
                postalCode = "12345",
                city = "CityOne",
                country = "CountryOne",
                latitude = 10.0,
                longitude = 20.0
            ),
            TheaterEntity(
                id = 2,
                name = "Theater Two",
                description = "Description Two",
                streetName = "Second Street",
                streetNumber = "2",
                postalCode = "67890",
                city = "CityTwo",
                country = "CountryTwo",
                latitude = 30.0,
                longitude = 40.0
            )
        )

        theaterDAO.upsertTheaters(theaters)
        val retrieved = theaterDAO.getTheaters()
        assertThat(retrieved).hasSize(2)
        assertThat(retrieved.map { it.id }).containsExactlyInAnyOrder(1, 2)
    }

    @Test
    fun getTheaterById_should_return_correct_theater() = runTest {
        val theater = TheaterEntity(
            id = 1,
            name = "Theater One",
            description = "Description One",
            streetName = "Main Street",
            streetNumber = "1",
            postalCode = "12345",
            city = "CityOne",
            country = "CountryOne",
            latitude = 10.0,
            longitude = 20.0
        )
        theaterDAO.upsertTheaters(listOf(theater))
        val retrieved = theaterDAO.getTheaterById(1)
        assertThat(retrieved).isNotNull
        assertThat(retrieved.name).isEqualTo("Theater One")
        assertThat(retrieved.description).isEqualTo("Description One")
        assertThat(retrieved.city).isEqualTo("CityOne")
    }
}