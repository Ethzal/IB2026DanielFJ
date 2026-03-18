package com.iberdrola.practicas2026.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.iberdrola.practicas2026.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : SettingsRepository {
    private val IS_LOCAL_KEY = booleanPreferencesKey("is_local_mode")

    override fun isLocalMode(): Flow<Boolean> = context.dataStore.data.map { it[IS_LOCAL_KEY] ?: true }

    override suspend fun toggleLocalMode(isLocal: Boolean) {
        context.dataStore.edit { it[IS_LOCAL_KEY] = isLocal }
    }
}