package com.kaleksandra.featuresearch.main.di

import com.kaleksandra.featuresearch.main.domain.SearchInteractor
import com.kaleksandra.featuresearch.main.domain.SearchInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface SearchModule {
    @Binds
    @ViewModelScoped
    fun provideSearchInteractor(impl: SearchInteractorImpl): SearchInteractor
}