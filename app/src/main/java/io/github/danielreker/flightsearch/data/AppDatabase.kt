package io.github.danielreker.flightsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.danielreker.flightsearch.data.daos.AirportDao
import io.github.danielreker.flightsearch.data.daos.RouteDao
import io.github.danielreker.flightsearch.data.entities.Airport
import io.github.danielreker.flightsearch.data.entities.Favorite

@Database(entities = [Airport::class, Favorite::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao
    abstract fun routeDao(): RouteDao
}