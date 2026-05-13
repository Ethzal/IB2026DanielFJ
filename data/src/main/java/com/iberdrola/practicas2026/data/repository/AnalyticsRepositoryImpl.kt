package com.iberdrola.practicas2026.data.repository

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.iberdrola.practicas2026.domain.repository.AnalyticsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AnalyticsRepository {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun logScreenView(screenName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    override fun logButtonClicked(buttonName: String) {
        val bundle = Bundle().apply {
            putString("button_name", buttonName)
        }
        firebaseAnalytics.logEvent("button_clicked", bundle)
    }
}