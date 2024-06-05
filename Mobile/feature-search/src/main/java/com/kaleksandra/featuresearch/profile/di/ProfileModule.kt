package com.kaleksandra.featuresearch.profile.di

import com.kaleksandra.featuresearch.profile.domain.ProfileInteractor
import com.kaleksandra.featuresearch.profile.domain.ProfileInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface ProfileModule {
    @Binds
    @ViewModelScoped
    fun provideProfileInteractor(impl: ProfileInteractorImpl): ProfileInteractor
}