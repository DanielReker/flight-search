package io.github.danielreker.flightsearch.ui

import io.github.danielreker.flightsearch.data.entities.Airport
import io.github.danielreker.flightsearch.data.model.Route


data class FlightSearchUiState(
    val searchQuery: String = "",
    val selectedAirport: Airport? = null,
    val airportSuggestions: List<Airport>? = null,
    val displayedRoutes: List<Route>? = null,
)