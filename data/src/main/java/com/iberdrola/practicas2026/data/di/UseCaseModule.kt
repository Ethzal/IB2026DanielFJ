package com.iberdrola.practicas2026.data.di

import com.iberdrola.practicas2026.domain.repository.FeedbackRepository
import com.iberdrola.practicas2026.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.domain.usecase.GetFeedbackStatusUseCase
import com.iberdrola.practicas2026.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.domain.usecase.UpdateFeedbackDecisionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetInvoicesUseCase(
        repository: InvoiceRepository
    ): GetInvoicesUseCase {
        return GetInvoicesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFeedbackStatusUseCase(repository: FeedbackRepository): GetFeedbackStatusUseCase {
        return GetFeedbackStatusUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateFeedbackDecisionUseCase(repository: FeedbackRepository): UpdateFeedbackDecisionUseCase {
        return UpdateFeedbackDecisionUseCase(repository)
    }
}