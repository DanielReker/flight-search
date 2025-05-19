package io.github.danielreker.flightsearch.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore("user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext appContext: Context
) {
    private val dataStore = appContext.dataStore

    private companion object {
        val SEARCH_QUERY = stringPreferencesKey("search_query")
    }

    suspend fun saveSearchQuery(searchQuery: String) {
        dataStore.edit { preferences ->
            preferences[SEARCH_QUERY] = searchQuery
        }
    }

    val searchQuery: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[SEARCH_QUERY]
        }
}