package com.nat.cineandroid.core.modules

import android.content.Context
import androidx.room.Room
import com.nat.cineandroid.BuildConfig
import com.nat.cineandroid.core.api.nat.NATCinemasAPI
import com.nat.cineandroid.core.cache.ApplicationCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NATCinemasAPI {
        return retrofit.create(NATCinemasAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ApplicationCache {
        return Room.databaseBuilder(
            context,
            ApplicationCache::class.java,
            "cine.db"
        ).fallbackToDestructiveMigration().build()
    }
}