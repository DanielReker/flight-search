package io.github.danielreker.flightsearch.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "iata_code")
    val iataCode: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "passengers")
    val passengers: Int,
)
