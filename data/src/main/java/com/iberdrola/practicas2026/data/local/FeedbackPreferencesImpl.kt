package com.iberdrola.practicas2026.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.iberdrola.practicas2026.domain.repository.FeedbackRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "feedback_prefs")

@Singleton
class FeedbackRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : FeedbackRepository {

    private object Keys {
        val EXIT_COUNT = intPreferencesKey("exit_count")
        val NEXT_SHOW_COUNT = intPreferencesKey("next_show_count")
    }

    override fun shouldShowFeedback(): Flow<Boolean> = context.dataStore.data.map { prefs ->
        val current = prefs[Keys.EXIT_COUNT] ?: 0
        val next = prefs[Keys.NEXT_SHOW_COUNT] ?: 0
        current >= next
    }

    override suspend fun incrementExitCount() {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.EXIT_COUNT] ?: 0
            prefs[Keys.EXIT_COUNT] = current + 1
        }
    }

    override suspend fun setNextShowDelay(delay: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.EXIT_COUNT] ?: 0
            prefs[Keys.NEXT_SHOW_COUNT] = current + delay
        }
    }
}