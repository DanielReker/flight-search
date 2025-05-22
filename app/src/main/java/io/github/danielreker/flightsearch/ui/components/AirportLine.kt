package io.github.danielreker.flightsearch.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import io.github.danielreker.flightsearch.data.entities.Airport

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