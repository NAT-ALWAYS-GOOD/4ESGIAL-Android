package com.nat.cineandroid.core.modules

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.nat.cineandroid.BuildConfig
import com.nat.cineandroid.core.api.AuthInterceptor
import com.nat.cineandroid.core.api.JwtTokenProvider
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.core.cache.ApplicationCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideAuthInterceptor(jwtTokenProvider: JwtTokenProvider): AuthInterceptor =
        AuthInterceptor(jwtTokenProvider)

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NATCinemasAPI =
        retrofit.create(NATCinemasAPI::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ApplicationCache =
        Room.databaseBuilder(
            context,
            ApplicationCache::class.java,
            "cine.db"
        ).fallbackToDestructiveMigration().build()
}