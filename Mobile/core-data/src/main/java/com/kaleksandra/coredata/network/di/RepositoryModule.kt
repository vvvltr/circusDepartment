package com.kaleksandra.coredata.network.di

import com.kaleksandra.coredata.network.repository.AuthRepository
import com.kaleksandra.coredata.network.repository.AuthRepositoryImpl
import com.kaleksandra.coredata.network.repository.ProfileRepository
import com.kaleksandra.coredata.network.repository.ProfileRepositoryImpl
import com.kaleksandra.coredata.network.repository.SearchRepository
import com.kaleksandra.coredata.network.repository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun provideSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}