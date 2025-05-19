package io.github.danielreker.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.danielreker.flightsearch.R
import io.github.danielreker.flightsearch.data.entities.Airport
import io.github.danielreker.flightsearch.data.model.Route
import io.github.danielreker.flightsearch.ui.theme.FlightSearchTheme


@Composable
fun App(
    viewModel: FlightSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FlightSearchScreen(
        uiState = uiState,
        onQueryChanged = viewModel::onQueryChanged,
        onAirportSelected = viewModel::onAirportSelected,
        onFavoriteClick = viewModel::onToggleFavorite
    )
}

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
                modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onQueryChanged,
                    placeholder = { Text("Enter departure airport") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { onQueryChanged("") },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Clear search",
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
                            Text("No airports found")
                        } else {
                            CircularProgressIndicator()
                        }
                    }
                    is FlightSearchUiState.FavoriteRoutes -> {
                        RouteList(
                            title =
                                if (uiState.displayedRoutes?.isNotEmpty() == true) "Favorite routes"
                                else "No favorite routes",
                            onFavoriteClick = onFavoriteClick,
                            routes = uiState.displayedRoutes,
                        )
                    }
                    is FlightSearchUiState.RoutesFromAirport -> {
                        RouteList(
                            title = "Flights from ${uiState.selectedAirport.iataCode}",
                            onFavoriteClick = onFavoriteClick,
                            routes = uiState.displayedRoutes,
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RouteList(
    modifier: Modifier = Modifier,
    title: String,
    routes: List<Route>? = null,
    onFavoriteClick: (route: Route) -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
        Spacer(Modifier.height(24.dp))

        if (routes != null) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(routes) { route ->
                    RouteCard(
                        route = route,
                        modifier = Modifier,
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }
        } else {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun RouteCard(
    modifier: Modifier = Modifier,
    route: Route,
    onFavoriteClick: (route: Route) -> Unit = {},
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                RouteCardAirport(caption = "DEPART", airport = route.departure)
                Spacer(Modifier.height(8.dp))
                RouteCardAirport(caption = "ARRIVE", airport = route.destination)
            }
            Spacer(Modifier.width(32.dp))
            IconButton(
                onClick = { onFavoriteClick(route) },
                modifier = Modifier.width(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Favorite button",
                    modifier = Modifier.fillMaxSize(),
                    tint = if (route.isFavorite) Color.Yellow else Color.Gray,
                )
            }
        }
    }
}



@Composable
fun RouteCardAirport(
    caption: String,
    airport: Airport,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(text = caption, style = TextStyle(fontWeight = FontWeight.Normal))
        AirportLine(airport = airport)
    }
}

@Composable
fun AirportLine(
    airport: Airport,
    modifier: Modifier = Modifier
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(airport.iataCode)
            }
            append("  ")
            withStyle(style = SpanStyle(fontWeight = FontWeight.Light)) {
                append(airport.name)
            }
        },
        modifier = modifier
    )
}




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
    FlightSearchScreen(FlightSearchUiState.FavoriteRoutes(
        searchQuery = "query",
        displayedRoutes = listOf(previewRoute)
    ))
}

@Preview(showBackground = true)
@Composable
fun RouteCardPreview() {
    RouteCard(route = previewRoute)
}
