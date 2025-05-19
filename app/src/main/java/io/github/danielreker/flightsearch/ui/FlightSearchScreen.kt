package io.github.danielreker.flightsearch.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.danielreker.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun App(
    viewModel: FlightSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FlightSearchScreen(uiState = uiState, onQueryChanged = { viewModel.onQueryChanged(it) })
}

@Composable
fun FlightSearchScreen(uiState: FlightSearchUiState, onQueryChanged: (query: String) -> Unit = {}) {
    FlightSearchTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                TextField(value = uiState.searchQuery, onValueChange = onQueryChanged)
                Text(text = uiState.toString())
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FlightSearchScreenPreview() {
    FlightSearchScreen(FlightSearchUiState())
}
