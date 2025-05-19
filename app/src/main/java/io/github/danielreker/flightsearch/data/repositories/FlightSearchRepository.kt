package io.github.danielreker.flightsearch.data.repositories

import io.github.danielreker.flightsearch.data.daos.AirportDao
import io.github.danielreker.flightsearch.data.daos.RouteDao
import io.github.danielreker.flightsearch.data.entities.Airport
import io.github.danielreker.flightsearch.data.entities.Favorite
import io.github.danielreker.flightsearch.data.model.Route
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlightSearchRepository @Inject constructor(
    private val airportDao: AirportDao,
    private val routeDao: RouteDao,
) {
    fun searchAirports(searchQuery: String): Flow<List<Airport>> = airportDao.searchAirports(searchQuery)

    fun getAllFavoriteRoutes(): Flow<List<Route>> = routeDao.getAllFavoriteRoutes()

    fun getRoutesFromAirport(departureIataCode: String): Flow<List<Route>> = routeDao.getRoutesFromAirport(departureIataCode)

    suspend fun addFavorite(favorite: Favorite) = routeDao.addFavorite(favorite)

    suspend fun deleteFavorite(departureCode: String, destinationCode: String) = routeDao.deleteFavorite(departureCode, destinationCode)
}