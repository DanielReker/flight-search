package io.github.danielreker.flightsearch.ui

import io.github.danielreker.flightsearch.data.entities.Airport
import io.github.danielreker.flightsearch.data.model.Route


sealed interface FlightSearchUiState {
    val searchQuery: String

    data class FavoriteRoutes(
        override val searchQuery: String = "",
        val displayedRoutes: List<Route>? = null,
    ) : FlightSearchUiState

    data class RoutesFromAirport(
        override val searchQuery: String = "",
        val selectedAirport: Airport,
        val displayedRoutes: List<Route>? = null,
    ) : FlightSearchUiState

    data class Airports(
        override val searchQuery: String = "",
        val airportSuggestions: List<Airport>? = null,
    ) : FlightSearchUiState
}