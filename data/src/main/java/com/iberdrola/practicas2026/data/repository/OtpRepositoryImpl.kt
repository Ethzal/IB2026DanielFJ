package com.iberdrola.practicas2026.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.iberdrola.practicas2026.domain.repository.OtpRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OtpRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : OtpRepository {

    private val ATTEMPTS_KEY = intPreferencesKey("otp_attempts")
    private val LAST_UPDATE_KEY = longPreferencesKey("otp_last_update")

    override val otpAttempts: Flow<Pair<Int, Long>> = context.dataStore.data.map {
        val attempts = it[ATTEMPTS_KEY] ?: 3
        val time = it[LAST_UPDATE_KEY] ?: 0L
        attempts to time
    }

    override suspend fun saveOtpAttempts(attempts: Int, lastTime: Long) {
        context.dataStore.edit {
            it[ATTEMPTS_KEY] = attempts
            it[LAST_UPDATE_KEY] = System.currentTimeMillis()
        }
    }
}