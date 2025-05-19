package io.github.danielreker.flightsearch

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.danielreker.flightsearch.data.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ) = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "flight_search"
    )
        .createFromAsset("database/flight_search.db")
        .build()

    @Singleton
    @Provides
    fun provideAirportDao(db: AppDatabase) = db.airportDao()

    @Singleton
    @Provides
    fun provideRouteDao(db: AppDatabase) = db.routeDao()
}