package com.iberdrola.practicas2026.data.repository

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.iberdrola.practicas2026.domain.repository.RemoteConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepositoryImpl @Inject constructor() : RemoteConfigRepository {
    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    
    private val _isGasEnabled = MutableStateFlow(true)
    override val isGasEnabled: StateFlow<Boolean> = _isGasEnabled.asStateFlow()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(mapOf("is_gas_enabled" to true))
        
        _isGasEnabled.value = firebaseRemoteConfig.getBoolean("is_gas_enabled")
    }

    override suspend fun fetchAndActivate() {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _isGasEnabled.value = firebaseRemoteConfig.getBoolean("is_gas_enabled")
            } else {
                Log.e("RemoteConfig", "Fetch failed")
            }
        }
    }
}