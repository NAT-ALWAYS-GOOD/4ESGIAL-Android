package com.nat.cineandroid.core.api.nat.service

import com.nat.cineandroid.core.api.nat.dto.user.LoginRequestDTO
import com.nat.cineandroid.core.api.nat.dto.user.LoginResponseDTO
import com.nat.cineandroid.core.api.nat.dto.user.RegisterRequestDTO
import com.nat.cineandroid.core.api.nat.dto.user.RegisterResponseDTO
import com.nat.cineandroid.core.api.nat.dto.user.UserResponseDTO
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.Response

class UserServiceTest {
    @Mock
    private lateinit var userService: UserService

    @Before
    fun setUp() {
        userService = Mockito.mock(UserService::class.java)
    }

    @Test
    fun `should login successfully`() = runTest {
        val loginRequest = LoginRequestDTO(
            username = "username",
            password = "secret"
        )
        val fakeLoginResponse = LoginResponseDTO(
            user = UserResponseDTO(
                id = 42,
                username = "username",
                password = "password",
                isActive = true,
                reservations = emptyList(),
                favoriteTheater = null
            ),
            accessToken = "fakeToken123"
        )

        `when`(userService.login(loginRequest)).thenReturn(Response.success(fakeLoginResponse))

        val response = userService.login(loginRequest)
        assertThat(response.isSuccessful).isTrue
        val body = response.body()
        assertThat(body).isNotNull
        assertThat(body!!.accessToken).isEqualTo("fakeToken123")
        assertThat(body.user.id).isEqualTo(42)
    }

    @Test
    fun `should register successfully`() = runTest {
        val registerRequest = RegisterRequestDTO(
            username = "newUser",
            password = "password"
        )
        val fakeRegisterResponse =
            RegisterResponseDTO(
                user = UserResponseDTO(
                    id = 42,
                    username = "username",
                    password = "password",
                    isActive = true,
                    reservations = emptyList(),
                    favoriteTheater = null
                ),
                accessToken = "fakeToken123"
            )

        `when`(userService.register(registerRequest)).thenReturn(
            Response.success(
                fakeRegisterResponse
            )
        )

        val response = userService.register(registerRequest)
        assertThat(response.isSuccessful).isTrue
        val body = response.body()
        assertThat(body).isNotNull
        assertThat(body!!.accessToken).isEqualTo("fakeToken123")
        assertThat(body.user.id).isEqualTo(42)
    }
}