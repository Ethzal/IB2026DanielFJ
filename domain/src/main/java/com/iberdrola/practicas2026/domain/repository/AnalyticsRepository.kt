package com.iberdrola.practicas2026.domain.repository

interface AnalyticsRepository {
    fun logScreenView(screenName: String)
    fun logButtonClicked(buttonName: String)
}