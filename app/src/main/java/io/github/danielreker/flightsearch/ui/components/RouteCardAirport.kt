package io.github.danielreker.flightsearch.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import io.github.danielreker.flightsearch.data.entities.Airport

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