package com.nat.cineandroid.core.api

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.nat.cineandroid.data.user.entity.UserClaims
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtTokenProvider @Inject constructor(@ApplicationContext private val context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs = EncryptedSharedPreferences.create(
        context,
        "secure_tokens",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getToken(): String? {
        return sharedPrefs.getString("jwt_token", null)
    }

    fun saveToken(token: String) {
        sharedPrefs.edit().putString("jwt_token", token).apply()
    }

    fun clearToken() {
        sharedPrefs.edit().remove("jwt_token").apply()
    }

    fun getUserClaimsFromJwt(jwt: String): UserClaims? {
        return try {
            val parts = jwt.split(".")
            if (parts.size < 2) return null

            val charset: Charset = Charset.forName("UTF-8")
            val payload =
                String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
            val jsonObject = JSONObject(payload)
            val id = jsonObject.optString("id")
            val username = jsonObject.optString("username")
            UserClaims(id, username)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
