package com.nat.cineandroid.core.api.nat.dto.user

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LoginRequestDTOTest {
    private val gson = Gson()

    private val username = "username"
    private val password = "password"

    private val jsonInput = """
        {
            "username": "$username",
            "password": "$password"
        }
    """.trimIndent()

    @Test
    fun `should deserialize JSON to LoginRequestDTO correctly`() {
        val dto = gson.fromJson(jsonInput, LoginRequestDTO::class.java)

        assertThat(dto).isNotNull
        assertThat(dto.username).isEqualTo(username)
        assertThat(dto.password).isEqualTo(password)
    }

    @Test
    fun `should serialize LoginRequestDTO to JSON correctly`() {
        val dto = LoginRequestDTO(
            username = username,
            password = password
        )

        val jsonOutput = gson.toJson(dto)

        val dtoFromOutput = gson.fromJson(jsonOutput, LoginRequestDTO::class.java)
        val dtoFromInput = gson.fromJson(jsonInput, LoginRequestDTO::class.java)

        assertThat(dtoFromOutput).isEqualTo(dtoFromInput)
    }
}