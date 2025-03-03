package com.nat.cineandroid.data.cinemaRoom
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nat.cineandroid.core.cache.ApplicationCache
import com.nat.cineandroid.data.cinemaRoom.dao.CinemaRoomDAO
import com.nat.cineandroid.data.cinemaRoom.entity.CinemaRoomEntity
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
class CinemaRoomDAOTest {
    private lateinit var database: ApplicationCache
    private lateinit var cinemaRoomDAO: CinemaRoomDAO
    private lateinit var theaterDAO: TheaterDAO

    @Before
    fun setUp() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ApplicationCache::class.java
        )
            .allowMainThreadQueries()
            .build()

        cinemaRoomDAO = database.cinemaRoomDao()
        theaterDAO = database.theaterDao()

        val theater = TheaterEntity(
            id = 100,
            name = "Theater Test",
            description = "Test description",
            streetName = "Test Street",
            streetNumber = "1",
            postalCode = "75000",
            city = "Paris",
            country = "France",
            latitude = 48.8566,
            longitude = 2.3522
        )

        val theater200 = TheaterEntity(
            id = 200,
            name = "Theater Test 200",
            description = "Test description 200",
            streetName = "Another Street",
            streetNumber = "2",
            postalCode = "75001",
            city = "Paris",
            country = "France",
            latitude = 48.8570,
            longitude = 2.3530
        )

        theaterDAO.upsertTheaters(listOf(theater, theater200))
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertRooms_should_insert_and_retrieve_cinema_rooms_by_theaterId() = runTest {
        val rooms = listOf(
            CinemaRoomEntity(
                id = 1,
                theaterId = 100,
                name = "Salle A",
                capacity = 50,
                description = "Salle A description",
                type = "Standard",
                isAccessible = true
            ),
            CinemaRoomEntity(
                id = 2,
                theaterId = 100,
                name = "Salle B",
                capacity = 75,
                description = "Salle B description",
                type = "IMAX",
                isAccessible = false
            ),
            CinemaRoomEntity(
                id = 3,
                theaterId = 200,
                name = "Salle C",
                capacity = 100,
                description = "Salle C description",
                type = "Standard",
                isAccessible = true
            )
        )

        // On insère ou met à jour les salles
        cinemaRoomDAO.upsertRooms(rooms)

        // On récupère les salles du théâtre avec id 100
        val roomsForTheater100 = cinemaRoomDAO.getCinemaRoomsByTheaterId(100)
        assertThat(roomsForTheater100).hasSize(2)
        assertThat(roomsForTheater100.map { it.name })
            .containsExactlyInAnyOrder("Salle A", "Salle B")
    }

    @Test
    fun getCinemaRoomsByIds_should_retrieve_correct_rooms() = runTest {
        val rooms = listOf(
            CinemaRoomEntity(
                id = 1,
                theaterId = 100,
                name = "Salle A",
                capacity = 50,
                description = "Salle A description",
                type = "Standard",
                isAccessible = true
            ),
            CinemaRoomEntity(
                id = 2,
                theaterId = 100,
                name = "Salle B",
                capacity = 75,
                description = "Salle B description",
                type = "IMAX",
                isAccessible = false
            ),
            CinemaRoomEntity(
                id = 3,
                theaterId = 200,
                name = "Salle C",
                capacity = 100,
                description = "Salle C description",
                type = "Standard",
                isAccessible = true
            )
        )

        cinemaRoomDAO.upsertRooms(rooms)

        val roomsByIds = cinemaRoomDAO.getCinemaRoomsByIds(listOf(1, 3))
        assertThat(roomsByIds).hasSize(2)
        assertThat(roomsByIds.map { it.id }).containsExactlyInAnyOrder(1, 3)
    }
}