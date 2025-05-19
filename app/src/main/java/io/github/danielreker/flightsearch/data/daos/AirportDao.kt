package io.github.danielreker.flightsearch.data.daos

import androidx.room.Dao
import androidx.room.Query
import io.github.danielreker.flightsearch.data.entities.Airport
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query("SELECT * FROM airport WHERE name LIKE :searchQuery || '%' OR iata_code LIKE :searchQuery || '%' ORDER BY passengers DESC")
    fun searchAirports(searchQuery: String): Flow<List<Airport>>
}