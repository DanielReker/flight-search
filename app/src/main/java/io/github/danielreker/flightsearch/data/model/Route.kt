package io.github.danielreker.flightsearch.data.model

import androidx.room.Embedded
import io.github.danielreker.flightsearch.data.entities.Airport

data class Route(
    @Embedded(prefix = "dep_")
    val departure: Airport,

    @Embedded(prefix = "dest_")
    val destination: Airport,

    val isFavorite: Boolean,
)
