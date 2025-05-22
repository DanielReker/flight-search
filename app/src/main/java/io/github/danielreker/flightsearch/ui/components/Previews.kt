package io.github.danielreker.flightsearch.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.danielreker.flightsearch.data.entities.Airport
import io.github.danielreker.flightsearch.data.model.Route
import io.github.danielreker.flightsearch.ui.FlightSearchUiState

private val previewRoute = Route(
    departure = Airport(
        id = 1, iataCode = "ABC",
        name = "First Airport preview with very very very very very very long name",
        passengers = 100
    ),
    destination = Airport(
        id = 2,
        iataCode = "DEF",
        name = "Second Airport preview",
        passengers = 200
    ),
    isFavorite = true
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FlightSearchScreenPreview() {
    FlightSearchScreen(
        FlightSearchUiState.FavoriteRoutes(
        searchQuery = "query",
        displayedRoutes = listOf(previewRoute)
    ))
}

@Preview(showBackground = true)
@Composable
fun RouteCardPreview() {
    RouteCard(route = previewRoute)
}