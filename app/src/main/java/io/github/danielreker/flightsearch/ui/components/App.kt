package io.github.danielreker.flightsearch.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.danielreker.flightsearch.ui.FlightSearchViewModel

@Composable
fun App(
    viewModel:
    FlightSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FlightSearchScreen(
        uiState = uiState,
        onQueryChanged = viewModel::onQueryChanged,
        onAirportSelected = viewModel::onAirportSelected,
        onFavoriteClick = viewModel::onToggleFavorite
    )
}