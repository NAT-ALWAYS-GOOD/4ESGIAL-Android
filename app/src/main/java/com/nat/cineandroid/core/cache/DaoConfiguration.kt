package com.nat.cineandroid.core.cache

import com.nat.cineandroid.data.cinemaRoom.dao.CinemaRoomDAO
import com.nat.cineandroid.data.movie.dao.MovieDAO
import com.nat.cineandroid.data.session.dao.SessionDAO
import com.nat.cineandroid.data.theater.dao.TheaterDAO
import com.nat.cineandroid.data.user.dao.UserDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoConfiguration {
    @Provides
    @Singleton
    fun provideCinemaRoomDao(database: ApplicationCache): CinemaRoomDAO = database.cinemaRoomDao()

    @Provides
    @Singleton
    fun provideMovieDao(database: ApplicationCache): MovieDAO = database.movieDao()

    @Provides
    @Singleton
    fun provideSessionDao(database: ApplicationCache): SessionDAO = database.sessionDao()

    @Provides
    @Singleton
    fun provideTheaterDao(database: ApplicationCache): TheaterDAO = database.theaterDao()

    @Provides
    @Singleton
    fun provideUserDao(database: ApplicationCache): UserDAO = database.userDao()
}