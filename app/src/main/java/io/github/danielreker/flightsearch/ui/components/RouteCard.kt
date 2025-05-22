package io.github.danielreker.flightsearch.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.danielreker.flightsearch.R
import io.github.danielreker.flightsearch.data.model.Route

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
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                RouteCardAirport(caption = stringResource(R.string.depart), airport = route.departure)
                Spacer(Modifier.height(8.dp))
                RouteCardAirport(caption = stringResource(R.string.arrive), airport = route.destination)
            }
            Spacer(Modifier.width(32.dp))
            IconButton(
                onClick = { onFavoriteClick(route) },
                modifier = Modifier.width(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = stringResource(R.string.favorite_button),
                    modifier = Modifier.fillMaxSize(),
                    tint = if (route.isFavorite) Color.Yellow else Color.Gray,
                )
            }
        }
    }
}