package io.github.danielreker.flightsearch.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.danielreker.flightsearch.R
import io.github.danielreker.flightsearch.data.entities.Airport
import io.github.danielreker.flightsearch.data.model.Route
import io.github.danielreker.flightsearch.ui.FlightSearchUiState
import io.github.danielreker.flightsearch.ui.theme.FlightSearchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchScreen(
    uiState: FlightSearchUiState,
    onQueryChanged: (query: String) -> Unit = {},
    onAirportSelected: (airport: Airport) -> Unit = {},
    onFavoriteClick: (route: Route) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    FlightSearchTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            topBar = { TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onQueryChanged,
                    placeholder = { Text(stringResource(R.string.enter_departure_airport)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = stringResource(R.string.search),
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { onQueryChanged("") },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = stringResource(R.string.clear_search),
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(24.dp))

                when (uiState) {
                    is FlightSearchUiState.Airports -> {
                        if (uiState.airportSuggestions?.isNotEmpty() == true) {
                            LazyColumn {
                                items(uiState.airportSuggestions) { airport ->
                                    AirportLine(
                                        airport = airport,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                focusManager.clearFocus()
                                                onAirportSelected(airport)
                                            }
                                    )
                                }
                            }
                        } else if (uiState.airportSuggestions != null) {
                            Text(stringResource(R.string.no_airports_found))
                        } else {
                            CircularProgressIndicator()
                        }
                    }
                    is FlightSearchUiState.FavoriteRoutes -> {
                        RouteList(
                            title =
                                if (uiState.displayedRoutes?.isNotEmpty() == true) stringResource(R.string.favorite_routes)
                                else stringResource(R.string.no_favorite_routes),
                            onFavoriteClick = onFavoriteClick,
                            routes = uiState.displayedRoutes,
                        )
                    }
                    is FlightSearchUiState.RoutesFromAirport -> {
                        RouteList(
                            title = stringResource(
                                R.string.flights_from,
                                uiState.selectedAirport.iataCode
                            ),
                            onFavoriteClick = onFavoriteClick,
                            routes = uiState.displayedRoutes,
                        )
                    }
                }
            }
        }
    }
}