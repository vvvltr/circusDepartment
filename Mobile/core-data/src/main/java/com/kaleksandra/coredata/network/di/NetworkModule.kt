package com.kaleksandra.coredata.network.di

import com.kaleksandra.coredata.network.api.AuthApi
import com.kaleksandra.coredata.network.api.ProfileApi
import com.kaleksandra.coredata.network.api.SearchApi
import com.kaleksandra.coredata.network.api.VacancyApi
import com.kaleksandra.coredata.network.builder
import com.kaleksandra.coredata.network.client
import com.kaleksandra.coredata.network.interceptors.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideSearchApi(): SearchApi =
        builder(
            "https://api.hh.ru/"
        ).client().build().create(SearchApi::class.java)

    @Provides
    @Singleton
    fun provideVacancyApi(authInterceptor: AuthInterceptor): VacancyApi =
        builder().client(listOf(authInterceptor)).build().create(VacancyApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi =
        builder().client().build().create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(): ProfileApi =
        builder().client().build().create(ProfileApi::class.java)
}