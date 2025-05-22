package io.github.danielreker.flightsearch.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.danielreker.flightsearch.data.model.Route

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