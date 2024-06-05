package com.taekwondo.featureauth.domain

import com.kaleksandra.coredata.network.Completable
import com.kaleksandra.coredata.network.Effect
import com.kaleksandra.coredata.network.repository.AuthRepository
import javax.inject.Inject

interface AuthInteractor {
    suspend fun auth(email: String, password: String): Effect<Completable>
    suspend fun register(
        email: String,
        password: String,
    ): Effect<Completable>
}

class AuthInteractorImpl @Inject constructor(
    private val authRepository: AuthRepository,
) : AuthInteractor {
    override suspend fun auth(email: String, password: String): Effect<Completable> {
        return authRepository.auth(email, password)
    }

    override suspend fun register(
        email: String,
        password: String,
    ): Effect<Completable> {
        return authRepository.register(email, password)
    }
}