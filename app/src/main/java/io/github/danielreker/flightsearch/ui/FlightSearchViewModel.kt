package io.github.danielreker.flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.danielreker.flightsearch.data.entities.Airport
import io.github.danielreker.flightsearch.data.entities.Favorite
import io.github.danielreker.flightsearch.data.model.Route
import io.github.danielreker.flightsearch.data.repositories.FlightSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val flightSearchRepository: FlightSearchRepository,
) : ViewModel() {

    private val _currentSearchQuery = MutableStateFlow("")
    private val _currentlySelectedAirport = MutableStateFlow<Airport?>(null)


    private val airportSuggestionsFlow: StateFlow<List<Airport>?> = _currentSearchQuery
        .debounce { if (it.isNotBlank()) 300L else 0L }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            flow {
                emit(null)
                if (query.isNotBlank() && _currentlySelectedAirport.value == null)
                    flightSearchRepository.searchAirports(query).collect { emit(it) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private val routesFromSelectedAirportFlow: StateFlow<List<Route>?> = _currentlySelectedAirport
        .flatMapLatest { selectedAirport ->
            flow<List<Route>?> {
                flowOf(null)
                if (selectedAirport != null)
                    flightSearchRepository.getRoutesFromAirport(selectedAirport.iataCode).collect { emit(it) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private val favoriteRoutesFlow: StateFlow<List<Route>?> = flightSearchRepository
        .getAllFavoriteRoutes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )


    val uiState: StateFlow<FlightSearchUiState> = combine(
        _currentSearchQuery,
        _currentlySelectedAirport,
        airportSuggestionsFlow,
        routesFromSelectedAirportFlow,
        favoriteRoutesFlow
    ) { searchQuery, selectedAirport, airportSuggestions, routesFromSelectedAirport, favoriteRoutes ->

        if (selectedAirport != null) {
            FlightSearchUiState.RoutesFromAirport(
                searchQuery = searchQuery,
                selectedAirport = selectedAirport,
                displayedRoutes = routesFromSelectedAirport
            )
        } else if (searchQuery.isNotBlank()) {
            FlightSearchUiState.Airports(
                searchQuery = searchQuery,
                airportSuggestions = airportSuggestions,
            )
        } else {
            FlightSearchUiState.FavoriteRoutes(
                searchQuery = searchQuery,
                displayedRoutes = favoriteRoutes
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = FlightSearchUiState.FavoriteRoutes(),
    )


    fun onQueryChanged(query: String) {
        _currentSearchQuery.update { query }

        val previouslySelected = _currentlySelectedAirport.value
        if (previouslySelected != null && query != previouslySelected.name && query != previouslySelected.iataCode) {
            _currentlySelectedAirport.update { null }
        }
        if (query.isBlank()){
            _currentlySelectedAirport.update { null }
        }
    }

    fun onAirportSelected(airport: Airport) {
        _currentlySelectedAirport.update { airport }
        _currentSearchQuery.update { airport.iataCode }
    }

    fun onToggleFavorite(route: Route) {
        viewModelScope.launch {
            if (route.isFavorite) {
                flightSearchRepository.deleteFavorite(
                    departureCode = route.departure.iataCode,
                    destinationCode = route.destination.iataCode
                )
            } else {
                val newFavorite = Favorite(
                    departureCode = route.departure.iataCode,
                    destinationCode = route.destination.iataCode,
                    id = 0
                )
                flightSearchRepository.addFavorite(newFavorite)
            }
        }
    }
}
