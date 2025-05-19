package io.github.danielreker.flightsearch.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.danielreker.flightsearch.data.entities.Favorite
import io.github.danielreker.flightsearch.data.model.Route
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Query("""
    SELECT
        dep_airport.id as dep_id, dep_airport.iata_code as dep_iata_code, dep_airport.name as dep_name, dep_airport.passengers as dep_passengers,
        dest_airport.id as dest_id, dest_airport.iata_code as dest_iata_code, dest_airport.name as dest_name, dest_airport.passengers as dest_passengers,
        1 as isFavorite
    FROM favorite fav
    JOIN airport dep_airport ON fav.departure_code = dep_airport.iata_code
    JOIN airport dest_airport ON fav.destination_code = dest_airport.iata_code
    ORDER BY fav.id DESC
    """)
    fun getAllFavoriteRoutes(): Flow<List<Route>>

    @Query("""
    SELECT
        dep_airport.id as dep_id, dep_airport.iata_code as dep_iata_code, dep_airport.name as dep_name, dep_airport.passengers as dep_passengers,
        dest_airport.id as dest_id, dest_airport.iata_code as dest_iata_code, dest_airport.name as dest_name, dest_airport.passengers as dest_passengers,
        CASE WHEN fav.id IS NOT NULL THEN 1 ELSE 0 END as isFavorite
    FROM airport dep_airport
    CROSS JOIN airport dest_airport
    LEFT JOIN favorite fav ON fav.departure_code = dep_airport.iata_code AND fav.destination_code = dest_airport.iata_code
    WHERE dep_airport.iata_code = :departureIataCode AND dep_airport.iata_code != dest_airport.iata_code
    ORDER BY dest_airport.name ASC
    """)
    fun getRoutesFromAirport(departureIataCode: String): Flow<List<Route>>

    @Insert
    suspend fun addFavorite(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    suspend fun deleteFavorite(departureCode: String, destinationCode: String)
}